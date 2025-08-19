package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.CompleteExamDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.StudentAnswerDTO;
import OneTransitionDemo.OneTransitionDemo.Models.*;
import OneTransitionDemo.OneTransitionDemo.Repositories.*;
import OneTransitionDemo.OneTransitionDemo.Services.ExamFileService;
import OneTransitionDemo.OneTransitionDemo.Services.StudentAnswerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private ExamFileService examFileService;
    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentAnswerService studentAnswerService;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveMultipleStudentAnswers(
            @AuthenticationPrincipal User user,
            @RequestPart("answers") String answersJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        try {
            System.out.println(">>> saveMultipleStudentAnswers CALLED");

            // ✅ Parse JSON into DTO list
            ObjectMapper objectMapper = new ObjectMapper();
            List<StudentAnswerDTO> answersList = objectMapper.readValue(
                    answersJson,
                    new TypeReference<List<StudentAnswerDTO>>() {}
            );

            System.out.println("Answers received: " + answersList.size());
            System.out.println("Files received: " + (files == null ? 0 : files.size()));

            // ✅ Map files to questionId (assuming filename format includes questionId, like "question_14_filename.docx")
            Map<Long, MultipartFile> fileMap = new HashMap<>();
            if (files != null) {
                for (MultipartFile file : files) {
                    String originalFilename = file.getOriginalFilename();  // ex: "14_test.docx"
                    if (originalFilename != null && originalFilename.contains("_")) {
                        try {
                            Long questionId = Long.parseLong(originalFilename.split("_")[0]);
                            fileMap.put(questionId, file);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }

            List<StudentAnswer> savedAnswers = new ArrayList<>();

            for (StudentAnswerDTO dto : answersList) {
                Long studentId = dto.getUserId();
                Long questionId = dto.getQuestionDTO().getId();

                System.out.println("Student ID: " + studentId);

                Optional <Question> qOp = questionRepository.findById(questionId);
                if(qOp.isEmpty()) {
                    return ResponseEntity.badRequest().body("Question not found: " + questionId);
                }
                Question question = qOp.get();
                Optional<User> studentOp = userRepository.findById(studentId);
                if(studentOp.isEmpty()) {
                    return ResponseEntity.badRequest().body("Student not found: " + studentId);
                }
                User student = studentOp.get();

                StudentAnswer answer = new StudentAnswer();
                Optional<Exam> examOp = examRepository.findById(dto.getExamId());
                if(examOp.isEmpty()) {
                    return ResponseEntity.badRequest().body("Exam not found: " + dto.getExamId());
                }
                Exam exam = examOp.get();
                answer.setExam(exam);
                answer.setStudent(student);
                answer.setQuestion(question);
                answer.setAnswerIndex(dto.getAnswerIndex());
                answer.setAnswerContent(dto.getAnswerContent());
                answer.setAnswerTrueFalse(dto.getAnswerTrueFalse() != null ? dto.getAnswerTrueFalse() : false);

                MultipartFile file = fileMap.get(questionId);
                if (file != null && !file.isEmpty()) {
                    String savedFilePath = examFileService.saveFile(file); // ✅ Call service
                    answer.setAnswerFilePath(savedFilePath);
                } else {
                    answer.setAnswerFilePath(dto.getAnswerFilePath());
                }

                answer.setSubmitAt(LocalDateTime.now());

                savedAnswers.add(studentAnswerRepository.save(answer));
            }

            return ResponseEntity.ok("Answers saved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error saving answers: " + e.getMessage());
        }
    }

}
