package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Models.ExamFile;
import OneTransitionDemo.OneTransitionDemo.Repositories.ExamFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ExamFileService {

    // Use your absolute folder path from your property or hardcoded
    @Value("${upload.path}")
    private Path uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        // Create directory if it does not exist
        Files.createDirectories(uploadDir);

        // Generate unique filename
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Resolve path for the file to save
        Path filePath = uploadDir.resolve(filename);

        // Copy file bytes to the path
        Files.copy(file.getInputStream(), filePath);

        System.out.println("Saving file to: " + filePath.toAbsolutePath()); // debug log
        // Return the file path or URL to save in DB
        // For example, if you want to save absolute path:
        // return filePath.toString();

        // Or if you plan to serve via HTTP from /files/ path:
        return  filename;
    }
}

