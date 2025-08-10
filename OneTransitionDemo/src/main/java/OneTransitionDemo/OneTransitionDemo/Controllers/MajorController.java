package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.MajorDTO;
import OneTransitionDemo.OneTransitionDemo.Models.Major;
import OneTransitionDemo.OneTransitionDemo.Repositories.MajorRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import OneTransitionDemo.OneTransitionDemo.Services.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/majors")
public class MajorController {

    @Autowired
    private MajorService majorService;

    @Autowired
    private MajorRepository majorRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllMajors(){
        Map<String, Object> response = majorService.getAllMajors();
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400 ).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllMajorss() {
        List<Major> majors = majorRepository.findAll();
        List<MajorDTO> dtos = majors.stream()
                .map(MajorDTO::new)
                .toList();
        return ResponseEntity.ok(ResponseUtil.success("Majors fetched", dtos));
    }
}
