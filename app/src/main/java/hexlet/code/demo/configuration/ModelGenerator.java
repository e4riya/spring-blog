package hexlet.code.demo.configuration;

import hexlet.code.demo.model.Post;
import hexlet.code.demo.model.User;
import hexlet.code.demo.repository.PostRepository;
import hexlet.code.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

@Component
public class ModelGenerator {
    private Faker faker;
    private UserRepository userRepository;
    private PostRepository postRepository;

    public ModelGenerator(Faker faker, PostRepository postRepository, UserRepository userRepository) {
        this.faker = faker;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void generateData(){
        for(int i = 0; i < 10; i++){
            var user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(faker.internet().emailAddress());
            userRepository.save(user);

            var post = new Post();
            String title;
            do {
                title = faker.book().title();
            } while (postRepository.existsByTitle(title));
            post.setTitle(title);

            post.setContent(faker.lorem().characters(200));;
            post.setPublished(faker.bool().bool());
            postRepository.save(post);
        }
    }
}
