package hexlet.code.demo.repository;

import hexlet.code.demo.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByPublishedTrue(Pageable pageable);
    boolean existsByTitle(String title);
    Optional<Post> findByTitle(String title);
}
