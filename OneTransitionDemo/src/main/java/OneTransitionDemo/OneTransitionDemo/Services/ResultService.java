package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.ResultDTO;
import OneTransitionDemo.OneTransitionDemo.Models.Result;
import OneTransitionDemo.OneTransitionDemo.Repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    public List<ResultDTO> getResultsByAssignmentAndExam(Long assignmentId, Long examId) {
        return resultRepository.findByAssignmentIdAndExamId(assignmentId, examId)
                .stream()
                .map(ResultDTO::new)
                .collect(Collectors.toList());
    }
}

