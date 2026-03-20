package hexlet.code.demo.controller;

import hexlet.code.demo.model.Post;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    private static List<Post> posts = new ArrayList<>();

    @GetMapping("/posts")
    public List<Post> index(@RequestParam(defaultValue = "10") Integer limit ){
        return posts.stream().limit(limit).toList();
    }
    @GetMapping("/posts/{id}")
    public Optional<Post> show(@PathVariable String id){
        return posts.stream().filter(p -> p.getSlug().equals(id)).findFirst();
    }
    @PostMapping("/posts")
    public Post create(@Valid @RequestBody Post post){
        posts.add(post);
        return post;
    }
    @PutMapping("/posts/{id}")
    public Post update(@PathVariable String id, @Valid @RequestBody Post data){
        var maybePost = posts.stream()
                             .filter(p -> p.getSlug().equals(id))
                             .findAny();
        if(maybePost.isPresent()){
            var page = maybePost.get();
            page.setSlug(data.getSlug());
            page.setTitle(data.getTitle());
            page.setContent(data.getContent());
            page.setAuthor(data.getAuthor());
        }
        return data;
    }
    @DeleteMapping("/posts/{id}")
    public void delete(@PathVariable String id){
        posts.removeIf(p -> p.getSlug().equals(id));
    }
}
