package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.MajorDTO;
import OneTransitionDemo.OneTransitionDemo.Repositories.MajorRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MajorService {

    @Autowired
    private MajorRepository majorRepository;

    public Map<String, Object> getAllMajors(){
        List<MajorDTO> Majors = majorRepository.findAll()
                .stream()
                .map(MajorDTO::new)
                .toList();
        return ResponseUtil.success("Majors found!", Majors);
    }
}
