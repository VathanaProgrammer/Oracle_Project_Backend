package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.DetailedResultDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.QuestionResultDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.ResultDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.StudentExamSummaryDTO;
import OneTransitionDemo.OneTransitionDemo.Models.*;
import OneTransitionDemo.OneTransitionDemo.Repositories.ExamRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.ResultRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.StudentAnswerRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ExamRepository examRepo;

    @Autowired
    private AnswerFeedbackService feedbackRepo;

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    @Autowired
    private StudentAnswerService studentAnswerRepo;

    public List<ResultDTO> getResultsByAssignmentAndExam(Long assignmentId, Long examId) {
        return resultRepository.findByAssignmentIdAndExamId(assignmentId, examId)
                .stream()
                .map(ResultDTO::new)
                .collect(Collectors.toList());
    }

    public DetailedResultDTO getDetailedResult(Long studentId, Long examId) {
        User student = userRepo.findById(studentId).orElseThrow();
        Exam exam = examRepo.findById(examId).orElseThrow();

        // Get all feedbacks
        List<AnswerFeedback> feedbacks = feedbackRepo.findByUserIdAndExamId(studentId, examId);

        // Get all student answers
        List<StudentAnswer> studentAnswers =
                studentAnswerRepository.findByExamIdAndUserId(examId, studentId);

        Map<Long, StudentAnswer> answerMap = studentAnswers.stream()
                .collect(Collectors.toMap(a -> a.getQuestion().getId(), a -> a));

        DetailedResultDTO dto = new DetailedResultDTO();
        dto.setStudentId(student.getId());
        dto.setProfilePicture(student.getProfilePicture());
        dto.setStudentName(student.getFirstname() + " " + student.getLastname());
        dto.setExamTitle(exam.getTitle());
        dto.setAssignmentInfo(exam.getAssignedTo() != null ? exam.getAssignedTo() : null);
        dto.setSubmittedAt(studentAnswers.isEmpty() ? null : studentAnswers.get(0).getSubmitAt());

        long totalScore = feedbacks.stream().mapToLong(AnswerFeedback::getScore).sum();
        dto.setTotalScore(totalScore);

        // Optional: get teacher comment from Result table
        Result result = resultRepository.findByStudentAndExam(student, exam).orElse(null);

        List<QuestionResultDTO> questionResults = feedbacks.stream().map(f -> {
            Question question = f.getQuestion();
            StudentAnswer sa = answerMap.get(question.getId());

            QuestionResultDTO q = new QuestionResultDTO();
            q.setQuestionId(question.getId());
            q.setText(question.getContent());
            q.setMaxScore(question.getScore());
            q.setScore(f.getScore());

            // 🔥 Always lowercase type with underscores
            String type = question.getType().toLowerCase();
            q.setQuestionType(type);

            switch (type) {
                case "multiple_choice" -> {
                    q.setOptions(question.getOptions()); // assume entity has options
                    if (sa != null && sa.getAnswerIndex() != null) {
                        q.setStudentAnswer(sa.getAnswerIndex().toString());
                    }
                    q.setCorrectAnswer(question.getCorrectAnswerIndex().toString());
                }
                case "true_false" -> {
                    if (sa != null && sa.getAnswerTrueFalse() != null) {
                        q.setStudentBooleanAnswer(sa.getAnswerTrueFalse());
                        q.setStudentAnswer(sa.getAnswerTrueFalse().toString());
                    }
                    if (question.getCorrectAnswer() != null) {
                        q.setCorrectBooleanAnswer(Boolean.parseBoolean(question.getCorrectAnswer()));
                        q.setCorrectAnswer(question.getCorrectAnswer());
                    }
                }
                case "short_answer" -> {
                    if (sa != null && sa.getAnswerContent() != null) {
                        q.setStudentAnswer(sa.getAnswerContent());
                    }
                    q.setCorrectAnswer(question.getCorrectAnswer());
                }
                case "file_exam" -> {
                    if (sa != null && sa.getAnswerFilePath() != null) {
                        q.setFileUrl(sa.getAnswerFilePath());
                        q.setStudentAnswer(sa.getAnswerFilePath());
                    }
                    q.setCorrectFileUrl(question.getCorrectAnswer()); // optional reference file
                }
                default -> {
                    if (sa != null && sa.getAnswerContent() != null) {
                        q.setStudentAnswer(sa.getAnswerContent());
                    }
                    q.setCorrectAnswer(question.getCorrectAnswer());
                }
            }

            return q;
        }).toList();

        dto.setQuestions(questionResults);

        return dto;
    }

    public List<StudentExamSummaryDTO> getStudentExamSummary(Long studentId) {
        // Fetch all results for this student
        List<Result> results = resultRepository.findByStudentId(studentId);

        List<StudentExamSummaryDTO> summaries = new ArrayList<>();

        for (Result result : results) {
            StudentExamSummaryDTO dto = new StudentExamSummaryDTO();
            dto.setExamId(result.getExam().getId());
            dto.setSubject(result.getAssignment().getSubject().getName());
            dto.setExamTitle(result.getExam().getTitle());
            dto.setTotalScore(result.getScore() != null ? result.getScore() : 0);
            dto.setMaxScore(result.getScore());
            dto.setSubmittedAt(result.getSubmittedAt());
            dto.setStatus(
                    dto.getTotalScore() >= dto.getMaxScore() * 0.5 ? "Pass" : "Fail"
            );
            summaries.add(dto);
        }

        return summaries;
    }
}

