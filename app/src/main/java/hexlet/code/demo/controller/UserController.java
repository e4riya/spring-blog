package hexlet.code.demo.controller;

import hexlet.code.demo.model.User;
import hexlet.code.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    /* Лучше так указывать статус, ResponseEntity устаревший и нужен
    когда заголовки нужно выставлять вручную или
    реализовывать какую то специфичную функциональность*/
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers(@RequestParam(defaultValue = "10") Integer limit) {
        return userRepository.findAll().stream().limit(limit).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        var savedUser = userRepository.save(user);
        return savedUser;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Valid @RequestParam Long id) {
        userRepository.deleteById(id);
    }

}
