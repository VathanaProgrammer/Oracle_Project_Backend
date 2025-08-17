package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.CompleteExamDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.StudentAnswerDTO;
import OneTransitionDemo.OneTransitionDemo.Models.*;
import OneTransitionDemo.OneTransitionDemo.Repositories.*;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Student student = studentRepository.findByUserId(studentId)
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
    public Map<String, Object> getAnswerStudent(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        List<StudentAnswerDTO>  studentAnswerDTOList = studentAnswerRepository.findDistinctStudentsByExam()
    }
}
