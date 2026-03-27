package hexlet.code.demo.controller;

import hexlet.code.demo.exception.ResourceNotFoundException;
import hexlet.code.demo.model.User;
import hexlet.code.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "firstName,asc") String sort) {
        /*можно юзануть как параметр Pageable,
        и тогда будет тоже самое только дефолты поменяются:
        page = 0 size = 20 сортировки не будет,
        так же можно юзануть Pageable и @PageableDefault и установить дефолты
        */
        String[] sortParts = sort.split(",");
        var field = sortParts[0];
        var direction = sortParts.length > 1 ? sortParts[1] : "asc";
        Sort sortObj = direction.equalsIgnoreCase("desc")
                       ? Sort.by(field).descending()
                       : Sort.by(field).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sortObj);
        return userRepository.findAll(pageable).getContent();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}
