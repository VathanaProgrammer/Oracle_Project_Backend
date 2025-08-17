package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import OneTransitionDemo.OneTransitionDemo.Models.Materials;
import OneTransitionDemo.OneTransitionDemo.Repositories.MaterialsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialsController {

    private final MaterialsRepository materialsRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public MaterialsController(MaterialsRepository materialsRepository) {
        this.materialsRepository = materialsRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<Materials> uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("username") String username,
            @RequestParam("role") Role role,
            @RequestParam("userProfile") String userProfile
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Ensure directory exists
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Create a unique file name
// Create a unique file name
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadPath, fileName);
            Files.write(path, file.getBytes());

            // Build material object
            Materials material = new Materials();
            material.setTitle(title);
            material.setUsername(username);
            material.setRole(role);
            material.setUserProfile(userProfile);
            material.setFileUrl(fileName); // absolute path (or could map to /uploads/ URL)
            material.setCreatedAt(LocalDateTime.now());

            // Save to DB
            Materials saved = materialsRepository.save(material);

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Get all materials
    @GetMapping
    public List<Materials> getAll() {
        return materialsRepository.findAll();
    }
}
