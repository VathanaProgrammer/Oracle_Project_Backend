package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.Exam;

import java.util.List;

// ExamDetailsDTO.jav

public class ExamDetailsDTO {
    private Long id;
    private String title;
    private String type;
    private List<QuestionDTO> questions;

    public ExamDetailsDTO(Exam exam) {
        this.id = exam.getId();
        this.title = exam.getTitle();
        this.type = exam.getType().name();
        this.questions = exam.getQuestions().stream()
                .map(QuestionDTO::new)
                .toList();
    }

    // getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public List<QuestionDTO> getQuestions() { return questions; }

}

