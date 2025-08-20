package OneTransitionDemo.OneTransitionDemo.DTO;

public class AnswerFeedbackDTO {
       private Long score;
       private Long userId;
       private Long examId;
       private Long questionId;

       // Constructors
       public AnswerFeedbackDTO() {}

       public AnswerFeedbackDTO(Long score, Long userId, Long examId, Long questionId) {
              this.score = score;
              this.userId = userId;
              this.examId = examId;
              this.questionId = questionId;
       }

       // Getters
       public Long getScore() { return score; }
       public Long getUserId() { return userId; }
       public Long getExamId() { return examId; }
       public Long getQuestionId() { return questionId; }

       // Setters
       public void setScore(Long score) { this.score = score; }
       public void setUserId(Long userId) { this.userId = userId; }
       public void setExamId(Long examId) { this.examId = examId; }
       public void setQuestionId(Long questionId) { this.questionId = questionId; }
}
