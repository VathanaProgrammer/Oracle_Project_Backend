package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.ExamAdminDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.ExamSummaryDTO;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import OneTransitionDemo.OneTransitionDemo.Repositories.ExamRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminExamService {

    @Autowired
    private ExamRepository examRepository;

    public Map<String, Object> getAllExams(){
        List<ExamAdminDTO> exams = examRepository.findAll()
                .stream()
                .map(ExamAdminDTO::new)
                .toList();
        return ResponseUtil.success("Exams found!", exams);
    }
}
