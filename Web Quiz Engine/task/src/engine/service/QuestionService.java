package engine.service;

import engine.entity.Question;
import engine.entity.User;
import engine.repo.QuestionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepo questionRepo;

    public QuestionService(QuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }

    public Question addQuestion(Question question, User user) {
        if (question == null) {
            return null;
        }
        question.setUser(user);

        return questionRepo.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    public boolean deleteQuestion(Question question, User user) {
        if (question == null || user.getId() != question.getUser().getId()) {
            return false;
        }
        questionRepo.delete(question);
        return true;
    }
}
