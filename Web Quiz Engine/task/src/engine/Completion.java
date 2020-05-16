package engine;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "completion")
public class Completion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long completionId;

    @Column(name = "question_id")
    @JsonProperty("id")
    private Long questionId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Completion() {
    }

    public Completion(Long questionId, Long userId) {
        this.questionId = questionId;
        this.userId = userId;
        this.completedAt = LocalDateTime.now();
    }

    public Long getCompletionId() {
        return completionId;
    }

    public void setCompletionId(Long completionId) {
        this.completionId = completionId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
