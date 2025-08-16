package OneTransitionDemo.OneTransitionDemo.DTO;
public class CompleteExamRequest {

    private Long userId;
    private Long examId;

    // Default constructor
    public CompleteExamRequest() {
    }

    // Parameterized constructor
    public CompleteExamRequest(Long userId, Long examId) {
        this.userId = userId;
        this.examId = examId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    @Override
    public String toString() {
        return "CompleteExamRequest{" +
                "userId=" + userId +
                ", examId=" + examId +
                '}';
    }
}
