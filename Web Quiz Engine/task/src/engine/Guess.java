package engine;

import java.util.ArrayList;
import java.util.List;

public class Guess {
    private List<Integer> answer = new ArrayList<>();

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

}