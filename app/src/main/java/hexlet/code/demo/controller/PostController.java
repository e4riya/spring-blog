package hexlet.code.demo.controller;

import hexlet.code.demo.model.Post;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private static List<Post> posts = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "10") Integer limit ){
        var res = posts.stream().limit(limit).toList();
        return ResponseEntity.ok().header("X-Total-Count",String.valueOf(posts.size())).body(res);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable String id){
        var res = posts.stream().filter(p -> p.getSlug().equals(id)).findFirst();
        return ResponseEntity.of(res);
    }
    @PostMapping
    public ResponseEntity<Post> create(@Valid @RequestBody Post post){
        posts.add(post);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(post.getSlug())
            .toUri();

        return ResponseEntity.created(location).body(post);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable String id, @Valid @RequestBody Post data){
        var maybePost = posts.stream()
                             .filter(p -> p.getSlug().equals(id))
                             .findAny();
        if(maybePost.isPresent()){
            var page = maybePost.get();
            page.setSlug(data.getSlug());
            page.setTitle(data.getTitle());
            page.setContent(data.getContent());
            page.setAuthor(data.getAuthor());
            return ResponseEntity.ok().body(data);
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        if(posts.removeIf(p -> p.getSlug().equals(id))){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
