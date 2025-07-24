package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.ResultDTO;
import OneTransitionDemo.OneTransitionDemo.Services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping
    public List<ResultDTO> getResults(@RequestParam Long assignmentId, @RequestParam Long examId) {
        return resultService.getResultsByAssignmentAndExam(assignmentId, examId);
    }
}

