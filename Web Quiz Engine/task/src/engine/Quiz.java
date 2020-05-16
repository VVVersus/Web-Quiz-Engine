package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class Quiz {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CompletionRepo completionRepo;

    @GetMapping(path = "/quizzes")
    public ResponseEntity<Page<Question>> getAllQuestions(@RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(defaultValue = "id") String sortBy) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));

        return new ResponseEntity<Page<Question>>(questionRepo.findAll(paging), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(path = "/quizzes")
    public Question addQuestion(@RequestBody @Valid Question question,
                                @AuthenticationPrincipal User user) {
        question.setUser(user);
        return questionRepo.save(question);
    }

    @GetMapping(path = "/quizzes/completed")
    public ResponseEntity<Page<Completion>> getCompletedQuestions(@AuthenticationPrincipal User user,
                                                                @RequestParam(defaultValue = "0") Integer page,
                                                                @RequestParam(defaultValue = "10") Integer pageSize) {

        Pageable paging = PageRequest.of(page, pageSize);

        return new ResponseEntity<Page<Completion>>(completionRepo.findAllByUserId(user.getId(), paging), new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/quizzes/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable(name = "id") Long id,
                                            @AuthenticationPrincipal User user) {
        Question question = questionRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (question.getUser().getId() == user.getId()) {
            questionRepo.delete(question);
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @GetMapping(path = "/quizzes/{id}")
    public Question getQuestion(@PathVariable(name = "id") Long id) {
        return questionRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(path = "/quizzes/{id}/solve")
    public Answer checkAnswer(@AuthenticationPrincipal User user,
                              @RequestBody Guess guess,
                              @PathVariable(name = "id") Long id) {
        Question question = questionRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (question.checkAnswer(guess)) {
            Completion completion = new Completion(id, user.getId());
            completionRepo.save(completion);
            return Answer.CORRECT_ANSWER;
        } else {
            return Answer.WRONG_ANSWER;
        }
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user) {
        try {
            if (userRepo.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.status(400).body(null);
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setActive(true);
                userRepo.save(user);
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
