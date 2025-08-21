package OneTransitionDemo.OneTransitionDemo.DTO;

import java.util.List;

public class QuestionResultDTO {
    private Long questionId;
    private String text;
    private String studentAnswer;
    private String correctAnswer;
    private Long score;
    private Integer maxScore;
    private String questionType; // e.g., "MCQ", "TRUE_FALSE", "SHORT_ANSWER", "FILE_UPLOAD"

    // ✅ For MCQ (store options)
    private List<String> options;

    // ✅ For True/False (studentAnswer + correctAnswer will still be used)
    private Boolean studentBooleanAnswer;
    private Boolean correctBooleanAnswer;

    // ✅ For Short Answer (studentAnswer + correctAnswer already used)

    // ✅ For File Upload (store file path or URL)
    private String fileUrl;
    private String correctFileUrl; // if teachers provide model answers

    // Getters & Setters
    public Long getQuestionId() {
        return questionId;
    }
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }
    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Long getScore() {
        return score;
    }
    public void setScore(Long score) {
        this.score = score;
    }

    public Integer getMaxScore() {
        return maxScore;
    }
    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public String getQuestionType() {
        return questionType;
    }
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<String> getOptions() {
        return options;
    }
    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Boolean getStudentBooleanAnswer() {
        return studentBooleanAnswer;
    }
    public void setStudentBooleanAnswer(Boolean studentBooleanAnswer) {
        this.studentBooleanAnswer = studentBooleanAnswer;
    }

    public Boolean getCorrectBooleanAnswer() {
        return correctBooleanAnswer;
    }
    public void setCorrectBooleanAnswer(Boolean correctBooleanAnswer) {
        this.correctBooleanAnswer = correctBooleanAnswer;
    }

    public String getFileUrl() {
        return fileUrl;
    }
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getCorrectFileUrl() {
        return correctFileUrl;
    }
    public void setCorrectFileUrl(String correctFileUrl) {
        this.correctFileUrl = correctFileUrl;
    }
}
