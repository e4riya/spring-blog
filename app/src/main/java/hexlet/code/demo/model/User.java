package hexlet.code.demo.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}
