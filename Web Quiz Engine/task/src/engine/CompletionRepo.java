package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletionRepo extends JpaRepository<Completion, Long>, PagingAndSortingRepository<Completion, Long> {

    @Query(value = "SELECT u FROM Completion u WHERE u.userId = :user_id ORDER BY u.completedAt DESC")
    Page<Completion> findAllByUserId(@Param("user_id") Long userId, Pageable pageable);
}
