package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.StudentAnswerDTO;
import OneTransitionDemo.OneTransitionDemo.Models.Question;
import OneTransitionDemo.OneTransitionDemo.Models.Student;
import OneTransitionDemo.OneTransitionDemo.Models.StudentAnswer;
import OneTransitionDemo.OneTransitionDemo.Repositories.QuestionRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.StudentAnswerRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/answers")
public class StudentAnswerController {

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<?> saveMultipleStudentAnswers(
            @RequestPart("answers") List<StudentAnswerDTO> answersDTO,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {
        try {
            // Map questionId -> file for quick lookup
            Map<Long, MultipartFile> fileMap = new HashMap<>();
            if (files != null) {
                for (MultipartFile file : files) {
                    String filename = file.getOriginalFilename();
                    if (filename != null) {
                        // Expect filename like "123_somefile.jpg" where 123 = questionId
                        String[] parts = filename.split("_", 2);
                        Long questionId = Long.parseLong(parts[0]);
                        fileMap.put(questionId, file);
                    }
                }
            }

            List<StudentAnswer> savedAnswers = new ArrayList<>();

            for (StudentAnswerDTO dto : answersDTO) {
                Long studentId = dto.getStudentDTO().getId();
                Long questionId = dto.getQuestionDTO().getId();

                Student student = studentRepository.findById(studentId)
                        .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
                Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));

                StudentAnswer answer = new StudentAnswer();
                answer.setStudent(student);
                answer.setQuestion(question);
                answer.setAnswerIndex(dto.getAnswerIndex());
                answer.setAnswerContent(dto.getAnswerContent());
                answer.setAnswerTrueFalse(dto.getAnswerTrueFalse());

                // Save file if exists for this question
                MultipartFile file = fileMap.get(questionId);
                if (file != null && !file.isEmpty()) {
                    String savedFilePath = saveFile(file);
                    answer.setAnswerFilePath(savedFilePath);
                } else {
                    answer.setAnswerFilePath(dto.getAnswerFilePath());
                }

                answer.setSubmitAt(LocalDateTime.now());

                savedAnswers.add(studentAnswerRepository.save(answer));
            }

            return ResponseEntity.ok(savedAnswers);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving answers: " + e.getMessage());
        }
    }

    // Helper method to save a file locally
    private String saveFile(MultipartFile file) throws IOException {
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path filePath = uploadDir.resolve(file.getOriginalFilename());
        file.transferTo(filePath.toFile());
        return filePath.toString();
    }
}
