package hexlet.code.demo.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Post {
    private String slug;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String author;
    private LocalDateTime createdAt;
}
