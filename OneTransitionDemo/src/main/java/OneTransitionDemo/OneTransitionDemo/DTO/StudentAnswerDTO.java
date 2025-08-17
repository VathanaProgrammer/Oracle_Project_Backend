package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.StudentAnswer;

public class StudentAnswerDTO {
    private Long studentId;
    private String Name;
    private String profile;
    private Long answerIndex;
    private String answerContent;
    private Boolean answerTrueFalse;
    private String answerFilePath;
    private QuestionDTO questionDTO;
    private StudentDTO studentDTO;
    private ExamDTO examDTO;
    public StudentAnswerDTO(StudentAnswer entity) {
        this.studentId = entity.getStudent().getId();
        this.Name = entity.getStudent().getName();
        this.profile = entity.getStudent().getUser().getProfilePicture();
        this.answerIndex = entity.getAnswerIndex();
        this.answerContent = entity.getAnswerContent();
        this.answerTrueFalse = entity.getAnswerTrueFalse();
        this.answerFilePath = entity.getAnswerFilePath();
        this.questionDTO = new QuestionDTO(entity.getQuestion());
        this.studentDTO = new StudentDTO(entity.getStudent());
        if (entity.getExam() != null) {
            this.examDTO = new ExamDTO(entity.getExam());
        }
    }
    // --- user info ---
    public Long getUserId() {
        return studentId;
    }

    public void setUserId(Long userId) {
        this.studentId = userId;
    }

    public String getFirstName() {
        return Name;
    }

    public void setFirstName(String firstName) {
        this.Name = Name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    // --- answer info ---
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

    public Boolean getAnswerTrueFalse() {
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

    // --- relations ---
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

