package engine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long>, PagingAndSortingRepository<Question, Long> {

    Optional<Question> findById(Long id);
    List<Question> findAll();
}