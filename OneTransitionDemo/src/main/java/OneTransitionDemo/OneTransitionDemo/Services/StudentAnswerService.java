package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.CompleteExamDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.ExamDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.QuestionDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.StudentAnswerDTO;
import OneTransitionDemo.OneTransitionDemo.Models.*;
import OneTransitionDemo.OneTransitionDemo.Repositories.*;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentAnswerService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExamRepository examRepository;

    @Transactional
    public void saveStudentAnswer(Long studentId, Long questionId, StudentAnswerDTO dto) {

        // 🔍 Step 1: Validate student and question
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student with ID " + studentId + " not found"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question with ID " + questionId + " not found"));
        // 🔒 Step 2: Validate answer based on question type
        String type = question.getType(); // Expected: "true_false", "multiple_choice", "short_answer", "file_exam"

        switch (type) {
            case "true_false":
                // Nothing to check - boolean always has value (default false), unless you want to enforce explicit sending
                break;

            case "multiple_choice":
                if (dto.getAnswerIndex() == null)
                    throw new IllegalArgumentException("Answer index is required for multiple choice question.");
                break;

            case "short_answer":
                if (dto.getAnswerContent() == null || dto.getAnswerContent().isBlank())
                    throw new IllegalArgumentException("Text answer is required for short answer question.");
                break;

            case "file_exam":
                if (dto.getAnswerFilePath() == null || dto.getAnswerFilePath().isBlank())
                    throw new IllegalArgumentException("File path is required for file-based answer.");
                break;

            default:
                throw new IllegalArgumentException("Unsupported question type: " + type);
        }

        // 📝 Step 3: Create and populate StudentAnswer entity
        StudentAnswer answer = new StudentAnswer();
        answer.setStudent(student);
        answer.setQuestion(question);
        answer.setAnswerIndex(dto.getAnswerIndex());
        answer.setAnswerContent(dto.getAnswerContent());
        answer.setAnswerTrueFalse(dto.getAnswerTrueFalse());
        answer.setAnswerFilePath(dto.getAnswerFilePath());
        answer.setSubmitAt(LocalDateTime.now());
        // 💾 Step 4: Save to DB
        studentAnswerRepository.save(answer);
    }
//    public Map<String, Object> getAnswerStudent(Long id){
//        Optional<User> userOptional = userRepository.findById(id);
//        List<StudentAnswerDTO>  studentAnswerDTOList = studentAnswerRepository.findDistinctStudentsByExam();
//    }
public Map<String, Object> getStudentAnswersWithQuestions(Long userId, Long examId) {
    List<Question> examQuestions = questionRepository.findByExamsId(examId);
    List<StudentAnswer> studentAnswers = studentAnswerRepository.findByExamIdAndUserId(examId, userId);

    Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new RuntimeException("Exam not found"));
    User student = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    ExamDTO examDTO = new ExamDTO(exam);
    examDTO.setQuestions(Collections.emptyList()); // avoid nested duplication

    Map<Long, StudentAnswer> answerMap = studentAnswers.stream()
            .collect(Collectors.toMap(sa -> sa.getQuestion().getId(), sa -> sa));

    List<StudentAnswerDTO> answerDTOs = examQuestions.stream().map(question -> {
        StudentAnswerDTO dto = new StudentAnswerDTO();
        StudentAnswer matchingAnswer = answerMap.get(question.getId());

        dto.setQuestionDTO(new QuestionDTO(question));
        dto.setStudentId(student.getId());
        dto.setName(student.getFirstname() + " " + student.getLastname());
        dto.setProfile(student.getProfilePicture());
        dto.setExamId(examId);

        if (matchingAnswer != null) {
            dto.setAnswerContent(matchingAnswer.getAnswerContent());
            dto.setAnswerIndex(matchingAnswer.getAnswerIndex());
            dto.setAnswerFilePath(matchingAnswer.getAnswerFilePath());

            // True/False questions
            if ("true_false".equalsIgnoreCase(question.getType()) && matchingAnswer.getAnswerTrueFalse() != null) {
                dto.setAnswerTrueFalse(matchingAnswer.getAnswerTrueFalse());
            }
            // Short answer questions
            else if (question.getCorrectAnswer() != null && matchingAnswer.getAnswerContent() != null) {
                dto.setAnswerTrueFalse(question.getCorrectAnswer().equalsIgnoreCase(matchingAnswer.getAnswerContent()));
            }
            // Multiple choice questions
            else if (question.getCorrectAnswerIndex() != null && matchingAnswer.getAnswerIndex() != null) {
                dto.setAnswerTrueFalse(question.getCorrectAnswerIndex().equals(matchingAnswer.getAnswerIndex()));
            }
            else {
                dto.setAnswerTrueFalse(false);
            }
        } else {
            dto.setAnswerTrueFalse(false);
        }


        return dto;
    }).collect(Collectors.toList());

    Map<String, Object> result = new HashMap<>();
    result.put("success", true);
    result.put("message", "Student answers found!");
    result.put("exam", examDTO);
    result.put("student", Map.of(
            "id", student.getId(),
            "firstName", student.getFirstname(),
            "lastName", student.getLastname(),
            "profile", student.getProfilePicture()
    ));
    result.put("answers", answerDTOs);

    return result;
}

}
