package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.DepartmentSummaryDTO;
import OneTransitionDemo.OneTransitionDemo.Repositories.DepartmentRepository;
import OneTransitionDemo.OneTransitionDemo.Services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllDepartments(){
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    // GET /api/departments/summary
    @GetMapping("/summary")
    public List<DepartmentSummaryDTO> getDepartmentsSummary() {
        return departmentService.getDepartmentsWithTeachersAndMajors();
    }
}
