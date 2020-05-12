package engine;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "question_id")
    private Long id;

    @Column
    @NotBlank(message = "Title is mandatory")
    private String title;

    @Column
    @NotBlank(message = "Text is mandatory")
    private String text;

    @Column
    @Size(min = 2)
    @NotNull
    @ElementCollection
    private List<String> options;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @ElementCollection
    private List<Integer> answer = new ArrayList<>();

    public Question() {
    }

    public Question(String title, String text, List<String> options, List<Integer> answer) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<Integer> answer) {
        this.answer = answer;
    }

    public boolean checkAnswer(ArrayList<Integer> answer) {
        return answer.equals(this.answer);
    }
}
