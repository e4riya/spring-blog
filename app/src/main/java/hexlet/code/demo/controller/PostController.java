package hexlet.code.demo.controller;

import hexlet.code.demo.model.Post;
import hexlet.code.demo.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "10") Integer limit ){
        var res = postRepository.findAll().stream().limit(limit).toList();
        return ResponseEntity.ok().header("X-Total-Count",String.valueOf(postRepository.count())).body(res);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable Long id){
        var res = postRepository.findById(id);
        return ResponseEntity.of(res);
    }
    @PostMapping
    public ResponseEntity<Post> create(@Valid @RequestBody Post post){
        postRepository.save(post);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(post.getId())
            .toUri();

        return ResponseEntity.created(location).body(post);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @Valid @RequestBody Post data){
        var maybePost = postRepository.findById(id);
        if(maybePost.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var post = maybePost.get();
        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setPublished(data.isPublished());

        postRepository.save(post);
        return ResponseEntity.ok(post);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if(!postRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
