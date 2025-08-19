package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.StudentAnswer;

public class StudentAnswerDTO {
    private Long userId;
    private Long studentId;
    private Long examId;
    private String name;       // From User entity
    private String profile;    // From User entity
    private Long answerIndex;
    private String answerContent;
    private Boolean answerTrueFalse;
    private String answerFilePath;
    private QuestionDTO questionDTO;
    private ExamDTO examDTO;

    public StudentAnswerDTO() {}

    public StudentAnswerDTO(StudentAnswer entity) {
        this.userId = entity.getStudent().getId();
        if (entity.getStudent() != null) {
            this.userId = entity.getStudent().getId();   // 👈 move inside
            this.studentId = entity.getStudent().getId();
            this.name = entity.getStudent().getFirstname() + " " + entity.getStudent().getLastname();
            this.profile = entity.getStudent().getProfilePicture();
        }

        if (entity.getExam() != null) {
            this.examId = entity.getExam().getId();
            this.examDTO = new ExamDTO(entity.getExam());
        }

        if (entity.getQuestion() != null) {
            this.questionDTO = new QuestionDTO(entity.getQuestion());
        }

        this.answerIndex = entity.getAnswerIndex();
        this.answerContent = entity.getAnswerContent();
        this.answerTrueFalse = entity.getAnswerTrueFalse();
        this.answerFilePath = entity.getAnswerFilePath();
    }

    // --- user info ---
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProfile() { return profile; }
    public void setProfile(String profile) { this.profile = profile; }

    // --- exam info ---
    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public ExamDTO getExamDTO() { return examDTO; }
    public void setExamDTO(ExamDTO examDTO) { this.examDTO = examDTO; }

    // --- answer info ---
    public Long getAnswerIndex() { return answerIndex; }
    public void setAnswerIndex(Long answerIndex) { this.answerIndex = answerIndex; }

    public String getAnswerContent() { return answerContent; }
    public void setAnswerContent(String answerContent) { this.answerContent = answerContent; }

    public Boolean getAnswerTrueFalse() { return answerTrueFalse; }
    public void setAnswerTrueFalse(Boolean answerTrueFalse) { this.answerTrueFalse = answerTrueFalse; }

    public String getAnswerFilePath() { return answerFilePath; }
    public void setAnswerFilePath(String answerFilePath) { this.answerFilePath = answerFilePath; }

    // --- relations ---
    public QuestionDTO getQuestionDTO() { return questionDTO; }
    public void setQuestionDTO(QuestionDTO questionDTO) { this.questionDTO = questionDTO; }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
