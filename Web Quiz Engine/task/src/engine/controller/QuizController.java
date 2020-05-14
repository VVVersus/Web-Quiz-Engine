package engine.controller;

import engine.entity.Answer;
import engine.entity.Question;
import engine.entity.QuizResult;
import engine.entity.User;
import engine.service.QuestionService;
import engine.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class QuizController {

    private final QuestionService questionService;
    private final UserService userService;

    public QuizController(QuestionService questionService, UserService userService) {
        this.questionService = questionService;
        this.userService = userService;
    }

    @GetMapping(path = "/quizzes", produces = "application/json")
    public List<Question> getAllQuizzes() {
        return questionService.getAllQuestions();
    }

    @PostMapping(path = "/quizzes", produces = "application/json")
    public Question insertQuiz(Principal principal,
                               @Valid @RequestBody Question question) {
        User user = userService.getUserByName(principal.getName());
        return questionService.addQuestion(question, user);
    }

    @DeleteMapping(path = "/quizzes/{question}", produces = "application/json")
    public ResponseEntity<Question> deleteQuestion(Principal principal,
                                                   @PathVariable(name = "question") Question question) {
        User user = userService.getUserByName(principal.getName());
        if (question == null) {
            return ResponseEntity
                    .status(404)
                    .body(null);
        }
        if (questionService.deleteQuestion(question, user)) {
            return ResponseEntity
                    .status(204)
                    .body(null);
        }
        return ResponseEntity
                .status(403)
                .body(null);
    }

    @GetMapping(path = "/quizzes/{question}", produces = "application/json")
    public ResponseEntity<Question> getQuiz(@PathVariable("question") Question question) {
        try {
            if (question != null) {
                return ResponseEntity
                        .ok(question);
            } else {
                return ResponseEntity
                        .status(404)
                        .body(null);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(404)
                    .body(null);
        }
    }

    @PostMapping(path = "/quizzes/{question}/solve", produces = "application/json")
    public ResponseEntity<QuizResult> solveQuiz(@PathVariable(name = "question") Question question,
                                                @RequestBody Answer answer) {
        try {
            if (question == null) {
                return ResponseEntity
                        .status(404)
                        .body(null);
            }
            if (question.checkAnswer(answer.getAnswer())) {
                return ResponseEntity
                .ok(new QuizResult(true));
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

    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            if (userService.addUser(user)) {
            return ResponseEntity
                    .ok()
                    .body(user);
            } else {
                return ResponseEntity
                        .status(400)
                        .body(null);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(400)
                    .body(null);
        }
    }

}
