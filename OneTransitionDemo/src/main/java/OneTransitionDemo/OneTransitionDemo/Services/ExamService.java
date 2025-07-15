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

    @Autowired
    private TeacherRepository teacherRepository;

    // Helper to avoid repeating ourselves for both new and update
    private void mapDTOToExam(Exam exam, ExamDTO dto) {
        exam.setTitle(dto.getTitle());
        exam.setDescription(dto.getDescription());
        exam.setType(ExamType.valueOf(dto.getType().toUpperCase()));
        exam.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
        exam.setStartTime(dto.getStartTime());
        exam.setEndTime(dto.getEndTime());
        exam.setDuration(dto.getDuration());
        exam.setDuration_unit(dto.getDurationUnit());

        Teacher teacher = teacherRepository.findByUserId(dto.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        exam.setTeacher(teacher);

        AssignedTo assignedTo = assignToRepository.findById(dto.getAssignTo())
                .orElseThrow(() -> new RuntimeException("AssignedTo not found"));
        exam.setAssignedTo(assignedTo);

        // Map questions
        List<Question> questions = dto.getQuestions().stream().map(qdto -> {
            Question q = new Question();
            q.setContent(qdto.getContent());
            q.setScore(qdto.getScore());
            q.setType(qdto.getType());
            q.setAutoScore(qdto.getAutoScore());
            q.setCorrectAnswer(qdto.getCorrectAnswer());
            q.setExams(List.of(exam));

            if ("multiple_choice".equalsIgnoreCase(qdto.getType())) {
                q.setOptions(qdto.getOptions());
                q.setCorrectAnswerIndex(qdto.getCorrectAnswerIndex());
            }

            if (qdto.getFileExams() != null) {
                List<ExamFile> examFiles = qdto.getFileExams().stream().map(fdto -> {
                    ExamFile ef = new ExamFile();
                    ef.setTitle(fdto.getTitle());
                    ef.setDescription(fdto.getDescription());
                    ef.setFileUrl(fdto.getFileUrl());
                    ef.setExam(exam);
                    ef.setQuestion(q);
                    return ef;
                }).collect(Collectors.toList());
                q.setExamFiles(examFiles);
            }

            return q;
        }).collect(Collectors.toList());

        exam.setQuestions(questions);
    }

    // for create new exam
    public Exam convertDTOToEntity(ExamDTO dto) {
        Exam exam = new Exam();
        mapDTOToExam(exam, dto);
        return examRepository.save(exam);
    }

    //for update an existed exam
    public Exam updateExamFromDTO(Long id, ExamDTO dto) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        exam.getQuestions().clear(); // Clear old questions
        mapDTOToExam(exam, dto);
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

    public List<ExamSummaryDTO> getDraftExams(){
        return examRepository.findByStatus(Status.DRAFT)
                .stream()
                .map(ExamSummaryDTO::new)
                .collect(Collectors.toList());
    }

    public List<ExamSummaryDTO> getCompletedExams(){
        return examRepository.findByStatus(Status.EXPIRED)
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
