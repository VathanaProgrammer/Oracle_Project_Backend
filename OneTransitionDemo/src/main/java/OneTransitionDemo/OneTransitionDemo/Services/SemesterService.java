package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Repositories.SemesterRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    public Map<String, Object> getAllSemesters(){
        return ResponseUtil.success("All Semesters found!", semesterRepository.findAll());
    }
}
