package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Repositories.AcademicRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AcademicService {

    @Autowired
    private AcademicRepository academicRepository;

    public Map<String, Object> getAllAcademicYears(){
        return ResponseUtil.success("Academic years found!", academicRepository.findAll());
    }
}
