package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.TeacherRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import OneTransitionDemo.OneTransitionDemo.Services.TeacherService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherInfo(@AuthenticationPrincipal User user, @PathVariable Long id){
        Map<String, Object> response = teacherService.getTeacherById(id);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

    @PutMapping(value = "/update-for-admin")
    public ResponseEntity<?> updateTeacherForAdmin(
            @AuthenticationPrincipal User user,
            @RequestParam("userId") Long userId,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("gender") String gender,
            @RequestParam(value = "departments", defaultValue = "[]") String departmentsJson,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        System.out.println("📥 Raw departments param: " + departmentsJson);

        List<Long> departmentIds = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            departmentIds = mapper.readValue(departmentsJson, new TypeReference<List<Long>>() {});
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(ResponseUtil.error("Invalid departments list"));
        }

        System.out.println("📥 Parsed department IDs: " + departmentIds);

        Map<String, Object> result = teacherService.updateForAdmin(
                userId, firstName, lastName, email, phone, gender, departmentIds, profilePicture
        );
        return ResponseEntity.ok(result);
    }



}
