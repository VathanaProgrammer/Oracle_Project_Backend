package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.ExamFile;
import OneTransitionDemo.OneTransitionDemo.Models.Question;

import java.util.List;

public class QuestionDTO {
    private String question; // this maps to entity's `content`
    private String type;
    private String content;
    private Boolean autoScore;
    private Integer score;
    private String correctAnswer;
    private Integer correctAnswerIndex;
    private List<String> options;
    private List<ExamFileDTO> fileExams;
    private Long id;

    public QuestionDTO() {}

    public QuestionDTO(Question q) {
        this.id = q.getId();
        this.content = q.getContent();
        this.type = q.getType();
        this.autoScore = q.getAutoScore();
        this.score = q.getScore();
        this.correctAnswer = q.getCorrectAnswer();
        this.correctAnswerIndex = q.getCorrectAnswerIndex();
        this.options = q.getOptions();

        // ✅ Include full ExamFileDTOs, not just file URLs
        if (q.getExamFiles() != null) {
            this.fileExams = q.getExamFiles().stream()
                    .map(file -> new ExamFileDTO(file.getTitle(), file.getDescription(), file.getFileUrl()))
                    .toList();
        } else {
            this.fileExams = List.of(); // safe fallback
        }

    }

    // GETTERS AND SETTERS
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Boolean getAutoScore() { return autoScore; }
    public void setAutoScore(Boolean autoScore) { this.autoScore = autoScore; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public Integer getCorrectAnswerIndex() { return correctAnswerIndex; }
    public void setCorrectAnswerIndex(Integer correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public List<ExamFileDTO> getFileExams() {
        return fileExams;
    }

    public void setFileExams(List<ExamFileDTO> fileExams) {
        this.fileExams = fileExams;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent(){ return content; }

}

