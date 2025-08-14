package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Services.AcademicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/academic-years")
public class AcademicController {

    @Autowired
    private AcademicService academicService;

    @GetMapping
    public ResponseEntity<?> getAllAcademicYears(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(academicService.getAllAcademicYears());
    }
}
