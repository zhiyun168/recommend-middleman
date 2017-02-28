package recommend.model;

/**
 * Created by ouduobiao on 16/6/28.
 */
public class RecItem {
    private String candidate;
    private Double score;


    public RecItem() {
    }

    public RecItem(String candidate, Double score) {
        this.candidate = candidate;
        this.score = score;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
