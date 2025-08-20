package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.AnswerFeedback;

public class AnswerFeedbackDTO {
       private Long Id;
       private Long score;
       private Long userId;
       private ExamDTO examDTO;
       private Long questionId;
       private UserDTO userDTO;

       public AnswerFeedbackDTO(AnswerFeedback answerFeedback) {
              this.Id = answerFeedback.getId();
              this.score = answerFeedback.getScore();

              if (answerFeedback.getExam() != null) {
                     this.examDTO = new ExamDTO(answerFeedback.getExam());
              }

              if (answerFeedback.getUser() != null) {
                     this.userDTO = new UserDTO(answerFeedback.getUser());
              }

              if (answerFeedback.getQuestion() != null) {
                     this.questionId = answerFeedback.getQuestion().getId();
              }
       }

       // Getters
       public Long getId() {
              return Id;
       }

       public Long getScore() {
              return score;
       }

       public ExamDTO getExamDTO() {
              return examDTO;
       }

       public Long getQuestionId() {
              return questionId;
       }

       public UserDTO getUserDTO() {
              return userDTO;
       }

       // Setters
       public void setId(Long id) {
              this.Id = id;
       }

       public void setScore(Long score) {
              this.score = score;
       }

       public void setExamDTO(ExamDTO examDTO) {
              this.examDTO = examDTO;
       }

       public void setQuestionId(Long questionId) {
              this.questionId = questionId;
       }

       public void setUserDTO(UserDTO userDTO) {
              this.userDTO = userDTO;
       }
       public Long getUserId(){
              return userDTO.getId();
       }
       public Long getExamId() {
              return examDTO != null ? examDTO.getId() : null;
       }

}
