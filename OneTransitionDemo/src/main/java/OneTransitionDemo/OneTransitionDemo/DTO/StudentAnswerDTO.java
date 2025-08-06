package OneTransitionDemo.OneTransitionDemo.DTO;

public class StudentAnswerDTO {
    private Long answerIndex;
    private String answerContent;
    private Boolean answerTrueFalse;
    private String answerFilePath;
    private QuestionDTO questionDTO;
    private StudentDTO studentDTO;

    public Long getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(Long answerIndex) {
        this.answerIndex = answerIndex;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public Boolean getAnswerTrueFalse() { // ✅ use get
        return answerTrueFalse;
    }

    public void setAnswerTrueFalse(Boolean answerTrueFalse) {
        this.answerTrueFalse = answerTrueFalse;
    }

    public String getAnswerFilePath() {
        return answerFilePath;
    }

    public void setAnswerFilePath(String answerFilePath) {
        this.answerFilePath = answerFilePath;
    }

    public QuestionDTO getQuestionDTO() {
        return questionDTO;
    }

    public void setQuestionDTO(QuestionDTO questionDTO) {
        this.questionDTO = questionDTO;
    }

    public StudentDTO getStudentDTO() {
        return studentDTO;
    }

    public void setStudentDTO(StudentDTO studentDTO) {
        this.studentDTO = studentDTO;
    }
}
