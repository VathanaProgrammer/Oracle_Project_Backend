package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.*;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Services.ExamFileService;
import OneTransitionDemo.OneTransitionDemo.Services.ExamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ExamService examService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExamFileService examFileService;

    @PostMapping
    public ResponseEntity<Exam> saveExam(
            @AuthenticationPrincipal User user,
            @RequestPart("examMeta") String examMetaJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
//        System.out.println(examMetaJson);
//        System.out.println("files: "+ files);
        ExamDTO examDTO = objectMapper.readValue(examMetaJson, ExamDTO.class); // this line convert a row JSON into a java object

        int fileIndex = 0;
        for (QuestionDTO question : examDTO.getQuestions()) { // convert DTO object into QuestionDTO object
            if ("file_exam".equalsIgnoreCase(question.getType())) {
                for (ExamFileDTO fileExam : question.getFileExams()) { // Convert each exam file object into ExamFileDTO
                    if (files != null && fileIndex < files.size()) { // Client sent this:
                                                                    // questions: [
                                                                      //            { type: "file_exam", fileExams: [ {}, {} ] }, // 2 files
                                                                       //           { type: "file_exam", fileExams: [ {} ] }       // 1 file
                                                                       //           ]
                        MultipartFile file = files.get(fileIndex);     // file object:  files = [ file1.pdf, file2.pdf, file3.pdf ];
                                                                        //| Loop | `fileIndex` | File used   | File assigned to        |
                                                                        //| ---- | ----------- | ----------- | ----------------------- |
                                                                        //| 1    | 0           | `file1.pdf` | question 0 → fileExam 0 |
                                                                        //| 2    | 1           | `file2.pdf` | question 0 → fileExam 1 |
                                                                        //| 3    | 2           | `file3.pdf` | question 1 → fileExam 0 |
                        //  This line saves the file to the upload/files folder, not database
                        String fileUrl = examFileService.saveFile(file);

                        //Then this sets the file name (or URL) into the ExamFileDTO
                        fileExam.setFileUrl(fileUrl); // after the first loop we go list of url or two object[]
                        fileIndex++;
                    }
                }
            }
        }

        Exam examReturn = examService.convertDTOToEntity(examDTO);

        return ResponseEntity.ok(examReturn);
    }

    @PutMapping("/{examId}")
    public ResponseEntity<Exam> updateExam(
            @PathVariable Long examId,
            @AuthenticationPrincipal User user,
            @RequestPart("examMeta") String examMetaJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        ExamDTO examDTO = objectMapper.readValue(examMetaJson, ExamDTO.class);

        // Assign files to fileExam DTOs before converting to Entity
        int fileIndex = 0;
        for (QuestionDTO question : examDTO.getQuestions()) {
            if ("file_exam".equalsIgnoreCase(question.getType())) {
                for (ExamFileDTO fileExam : question.getFileExams()) {
                    if (files != null && fileIndex < files.size()) {
                        MultipartFile file = files.get(fileIndex);
                        String fileUrl = examFileService.saveFile(file);
                        fileExam.setFileUrl(fileUrl);
                        fileIndex++;
                    }
                }
            }
        }

        // 🔁 Convert DTO to entity and update instead of creating new
        Exam updatedExam = examService.updateExamFromDTO(examId, examDTO);

        return ResponseEntity.ok(updatedExam);
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ExamDetailsDTO> getExamDetails(@PathVariable Long id) {
        return examService.getExamDetailsDTOById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/forAdmin/{id}", produces = "application/json")
    public ResponseEntity<ExamDetailsDTO> getExamDetailsForAdmin(@PathVariable Long id) {
        return examService.getExamDetailsDTOForAdminById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public List<ExamSummaryDTO> getAvailableExams(@AuthenticationPrincipal User user) {
        List<ExamSummaryDTO> examSummaryDTO= examService.getAvailableExamSummaries();
        System.out.println(examSummaryDTO.size());
        return examSummaryDTO;
    }

    @GetMapping("/draft")
    public List<ExamSummaryDTO> getDraftExams(@AuthenticationPrincipal User user) {
        System.out.println("Draft exams has requested!");
        List<ExamSummaryDTO> examSummaryDTO = examService.getDraftExams();
        return examSummaryDTO;
    }
    @GetMapping("/completed")
    public List<ExamSummaryDTO> getCompletedExams(@AuthenticationPrincipal User user) {
        List<ExamSummaryDTO> examSummaryDTO = examService.getCompletedExams();
        return examSummaryDTO;
    }

    @PutMapping("/{id}/end")
    public ResponseEntity<?> endExam(@AuthenticationPrincipal User user, @PathVariable Long id) {

        UserActionDTO dto = new UserActionDTO(user.getId(),
                user.getFirstname(),
                user.getLastname(),
                "Ended_exam",
                "Ended exam",
                user.getFirstname() + " "+ user.getLastname() + " just end an exam");

        Map<String, Object> response = examService.endExam(dto,id);
        messagingTemplate.convertAndSend("/topic/api/actions/recent", dto);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

    @GetMapping("/all")
    public List<ExamSummaryDTO> getAllExam( @AuthenticationPrincipal User user){
        return examService.getAllExam();
    }

    @GetMapping("/test")
    public List<ExamSummaryDTO> paddingExams() {
        List<ExamSummaryDTO> examSummaryDTO = examService.Exams();
        return examSummaryDTO;
    }
    @PutMapping("/canceled/{id}")
    public ResponseEntity<?> cancelExam(@AuthenticationPrincipal User user,@PathVariable Long id){
        Map<String, Object> response = examService.cancelExam(id);
        return  ResponseEntity.status((Boolean)response.get("success") ? 200:400).body(response);
    }



}
