package hexlet.code.demo.controller;

import hexlet.code.demo.model.Post;
import hexlet.code.demo.repository.PostRepository;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper om;

    @Test
    public void testGetPublishedPosts() throws Exception {
        mockMvc.perform(get("/api/posts"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray());

    }

    @Test
    public void testCreate() throws Exception {
        var post = Instancio.of(Post.class)
                            .ignore(Select.field(Post::getId))
                            .ignore(Select.field(Post::getCreatedAt))
                            .ignore(Select.field(Post::getUpdatedAt))
                            .create();

        var request = post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(post));

        mockMvc.perform(request)
               .andExpect(status().isCreated());

        var savedPost = postRepository.findByTitle(post.getTitle()).get();

        assertEquals(post.getContent(), savedPost.getContent());
        assertEquals(post.isPublished(), savedPost.isPublished());
        assertNotNull(savedPost.getId());
    }

    @Test
    public void testShow() throws Exception {
        var post = Instancio.of(Post.class)
                            .ignore(Select.field(Post::getId))
                            .ignore(Select.field(Post::getCreatedAt))
                            .ignore(Select.field(Post::getUpdatedAt))
                            .create();
        var savedPost = postRepository.save(post);

        mockMvc.perform(get("/api/posts/" + savedPost.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(savedPost.getId()))
               .andExpect(jsonPath("$.title").value(post.getTitle()))
               .andExpect(jsonPath("$.content").value(post.getContent()))
               .andExpect(jsonPath("$.published").value(post.isPublished()));
    }

    @Test
    public void testUpdate() throws Exception {
        var post = Instancio.of(Post.class)
                            .ignore(Select.field(Post::getId))
                            .ignore(Select.field(Post::getCreatedAt))
                            .ignore(Select.field(Post::getUpdatedAt))
                            .create();
        var savedPost = postRepository.save(post);
        var newTitle = "updated title";
        var newContent = "updated content";

        var data = new HashMap<>();
        data.put("title", newTitle);
        data.put("content", newContent);
        data.put("published", savedPost.isPublished());

        var request = put("/api/posts/" + savedPost.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(data));

        mockMvc.perform(request)
               .andExpect(status().isOk());

        post = postRepository.findById(savedPost.getId()).get();
        assertEquals(newTitle, post.getTitle());
        assertEquals(newContent, post.getContent());
    }

    @Test
    public void testDelete() throws Exception {
        var post = Instancio.of(Post.class)
                            .ignore(Select.field(Post::getId))
                            .ignore(Select.field(Post::getCreatedAt))
                            .ignore(Select.field(Post::getUpdatedAt))
                            .create();
        var savedPost = postRepository.save(post);

        var request = delete("/api/posts/" + savedPost.getId());
        mockMvc.perform(request).andExpect(status().isNoContent());

        assertFalse(postRepository.existsById(savedPost.getId()));
    }
}
