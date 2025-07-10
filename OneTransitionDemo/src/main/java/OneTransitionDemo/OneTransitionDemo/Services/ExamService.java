package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.ExamDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.ExamDetailsDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.ExamSummaryDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.UserActionDTO;
import OneTransitionDemo.OneTransitionDemo.ENUMS.ExamType;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Status;
import OneTransitionDemo.OneTransitionDemo.Models.*;
import OneTransitionDemo.OneTransitionDemo.Repositories.*;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import jakarta.persistence.ManyToMany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private AssignToRepository assignToRepository;

    @Autowired
    private UserActionRepository userActionRepository;

    public Exam convertDTOToEntity(ExamDTO dto) {
        Exam exam = new Exam();

        exam.setTitle(dto.getTitle());
        exam.setDescription(dto.getDescription());
        exam.setType(ExamType.valueOf(dto.getType().toUpperCase()));
        exam.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));

        //Query compare
        // INSERT INTO exams (title, description, start_time, end_time, duration, duration_unit)
        // VALUES ('Final Exam', 'End of term exam', '2025-06-20 09:00', '2025-06-20 10:00', 60, 'minutes');

        //find the user who create this exam
        //System.out.println("Looking for username: " + dto.getCreatedBy());
        User teacher = userRepository.findById(dto.getCreatedBy()).orElseThrow(
                () -> new RuntimeException("User not found with id: " + dto.getCreatedBy())  // SELECT * FROM users WHERE id = 5;
                                                                                                //-- Assume it returns ID = 5
                                                                                                //-- Then use that ID as foreign key
        );

        exam.setTeacher(teacher);
        exam.setStartTime(dto.getStartTime());
        exam.setEndTime(dto.getEndTime());

        exam.setDuration(dto.getDuration()); // it's just an int now
        exam.setDuration_unit(dto.getDurationUnit()); // simple string

        AssignedTo assignedTo = assignToRepository.findById(dto.getAssignTo()) // SELECT * FROM assigned_to WHERE id = 2;
                .orElseThrow(() -> new RuntimeException("AssignedTo not found"));
        exam.setAssignedTo(assignedTo);

        if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
            List<Question> questions = dto.getQuestions().stream() // Loop through the list of questions
                    .map(qdto -> {
                        Question q = new Question();
                        q.setContent(qdto.getQuestion());
                        q.setScore(qdto.getScore());
                        q.setType(qdto.getType());
                        q.setAutoScore(qdto.getAutoScore());
                        q.setCorrectAnswer(qdto.getCorrectAnswer());
                        q.setExams(List.of(exam)); // and this will create a middle table call tbl_exam_questions //
                                                    // Each Question knows it belongs to this Exam
                                                    //  @JsonIgnore
                                                    // @ManyToMany(mappedBy = "questions")
                                                    // private List<Exam> exams;
                        // ADD THIS to save multiple-choice options
                        if ("multiple_choice".equalsIgnoreCase(qdto.getType()) && qdto.getOptions() != null) {
                            q.setOptions(qdto.getOptions()); // set reference to the question_id and option
                                                            //    @ElementCollection(fetch = FetchType.EAGER)
                                                            //    @CollectionTable(
                                                            //            name = "TBL_QUESTION_OPTIONS",
                                                            //            joinColumns = @JoinColumn(name = "question_id")
                                                            //    )
                                                            //    @Column(name = "option_text")
                                                            //    private List<String> options;
                            q.setCorrectAnswerIndex(qdto.getCorrectAnswerIndex());
                        }


                        // Handle fileExams conversion
                        if (qdto.getFileExams() != null) { // Loop through all fileExams[] return like of fileExam object[]
                            List<ExamFile> examFiles = qdto.getFileExams().stream() // set reference to question in Question and ExamFile
                                    .map(fdto -> { // ExamFileDTO goes from QuestionDTO
                                        ExamFile ef = new ExamFile();
                                        ef.setTitle(fdto.getTitle());
                                        ef.setDescription(fdto.getDescription());
                                        ef.setFileUrl(fdto.getFileUrl()); // set list of fileUrl we set at the start
                                        ef.setExam(exam);  // set reference to the exam_id
                                        ef.setQuestion(q); // set reference to the question_id
                                        return ef;
                                    })
                                    .collect(Collectors.toList());
                            q.setExamFiles(examFiles);  // set file exam
                        }
                        return q; // when q is return the object questions will have a list of
                                    // question as long as the QuestionDTO is sent from the front end
                    }).collect(Collectors.toList());

            exam.setQuestions(questions);// set reference of exam_id and question_id
                                            // Exam knows its Questions
                                            //    @ManyToMany(cascade = CascadeType.ALL) // One Exam can have many question
                                            //    @JoinTable(name = "tbl_exam_questions",
                                            //            joinColumns = @JoinColumn(name = "exam_id"),
                                            //            inverseJoinColumns = @JoinColumn(name = "question_id"))
                                            //    private List<Question> questions;
        }

        return examRepository.save(exam);
    }

    public Optional<ExamDetailsDTO> getExamDetailsDTOById(Long id) {
        return examRepository.findExamWithQuestions(id)
                .map(exam -> {
                    // Manually initialize related bags
                    for (Question q : exam.getQuestions()) {
                        q.getOptions().size();       // forces loading of options
                        q.getExamFiles().size();     // forces loading of examFiles
                    }
                    return new ExamDetailsDTO(exam);
                });
    }

    public Optional<ExamDetailsDTO> getExamDetailsDTOForAdminById(Long id) {
        return examRepository.findExamWithQuestionsForAdmin(id)
                .map(exam -> {
                    // Manually initialize related bags
                    for (Question q : exam.getQuestions()) {
                        q.getOptions().size();       // forces loading of options
                        q.getExamFiles().size();     // forces loading of examFiles
                    }
                    return new ExamDetailsDTO(exam);
                });
    }

    public List<ExamSummaryDTO> getAvailableExamSummaries() {
        return examRepository.findByStatus(Status.PUBLISHED)
                .stream()
                .map(ExamSummaryDTO::new)
                .collect(Collectors.toList());
    }

    public Map<String, Object> endExam(UserActionDTO dto, Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        if (exam == null){
            return ResponseUtil.error("Exam not found!");
        }

        UserAction action = new UserAction();
        action.setLastName(dto.getLastName());
        action.setFirstName(dto.getFirstName());
        action.setLabel(dto.getLabel());
        action.setDescription(dto.getDescription());
        action.setType(dto.getType());
        action.setTimestamp(LocalDateTime.now());
        userActionRepository.save(action);

        exam.setStatus(Status.ENDED);
        examRepository.save(exam);

        return ResponseUtil.success("Exam was successfully ended.");
    }

}
