package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/quizzes")
public class QuizController {

    @Autowired
    QuestionRepository questionRepository;

    @GetMapping
    public List<Question> getAllQuizzes() {
        return questionRepository.findAll();
    }

    @PostMapping
    public Question insertQuiz(@Valid @RequestBody Question question) {
        return questionRepository.save(question);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Question> getQuiz(@PathVariable("id") Long id) {
        try {
            Question question = questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found for this id :: " + id));
            return ResponseEntity
                    .ok()
                    .body(question);
        } catch (Exception e) {
            return ResponseEntity
                    .status(404)
                    .body(null);
        }
    }

    @PostMapping(path = "/{id}/solve")
    public ResponseEntity<QuizResult> solveQuiz(@PathVariable Long id, @RequestBody Answer answer) {
        try {
            Question question = questionRepository.findById(id).orElse(null);
            if (question.checkAnswer(answer.getAnswer())) {
                return ResponseEntity
                .status(200)
                .body(new QuizResult(true));
            } else {
                return ResponseEntity
                .status(200)
                .body(new QuizResult(false));
            }
        } catch (IndexOutOfBoundsException e) {
            return ResponseEntity
                    .status(404)
                    .body(null);
        }
    }




}
