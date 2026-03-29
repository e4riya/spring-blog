package hexlet.code.demo.controller;

import hexlet.code.demo.model.User;
import hexlet.code.demo.repository.UserRepository;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper om;

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testCreateUser() throws Exception {
        var user = Instancio.of(User.class)
                            .ignore(Select.field(User::getId))
                            .ignore(Select.field(User::getCreatedAt))
                            .ignore(Select.field(User::getUpdatedAt))
                            .generate(Select.field(User::getFirstName), gen -> gen.string().maxLength(1).maxLength(50))
                            .generate(Select.field(User::getEmail), gen -> gen.net().email())
                            .create();

        var request = post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(user));

        mockMvc.perform(request)
               .andExpect(status().isCreated());

        var savedUser = userRepository.findByFirstName(user.getFirstName()).get();

        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertNotNull(savedUser.getId());
    }

    @Test
    public void testGetUser() throws Exception {
        var user = Instancio.of(User.class)
                            .ignore(Select.field(User::getId))
                            .ignore(Select.field(User::getCreatedAt))
                            .ignore(Select.field(User::getUpdatedAt))
                            .generate(Select.field(User::getFirstName), gen -> gen.string().maxLength(1).maxLength(50))
                            .generate(Select.field(User::getEmail), gen -> gen.net().email())
                            .create();
        var savedUser = userRepository.save(user);

        var request = get("/api/users/" + savedUser.getId());
        mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(user.getId()))
               .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(user.getLastName()))
               .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void testDeleteUser() throws Exception {
        var user = Instancio.of(User.class)
                            .ignore(Select.field(User::getId))
                            .ignore(Select.field(User::getCreatedAt))
                            .ignore(Select.field(User::getUpdatedAt))
                            .generate(Select.field(User::getFirstName), gen -> gen.string().maxLength(1).maxLength(50))
                            .generate(Select.field(User::getEmail), gen -> gen.net().email())
                            .create();
        var savedUser = userRepository.save(user);

        var request = delete("/api/users/" + savedUser.getId());
        mockMvc.perform(request).andExpect(status().isNoContent());
        assertFalse(userRepository.existsById(savedUser.getId()));
    }
}
