package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.UserActionDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.UserActionReportDTO;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Request.ExportRequest;
import OneTransitionDemo.OneTransitionDemo.Request.UserExportRequest;
import OneTransitionDemo.OneTransitionDemo.Services.AdminReportService;
import OneTransitionDemo.OneTransitionDemo.Services.UserActionService;
import OneTransitionDemo.OneTransitionDemo.Services.UserService;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private UserActionService userActionService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminReportService adminReportService;

    @PostMapping("/export/{format}")
    public ResponseEntity<InputStreamResource> exportSelected(
            @PathVariable String format,
            @RequestBody ExportRequest request
    ) throws Exception {

        List<UserActionReportDTO> actions = adminReportService.getUserActionReport();

        if (request.getIds() != null && !request.getIds().isEmpty()) {
            // filter by action ID
            actions = actions.stream()
                    .filter(a -> request.getIds().contains(a.getId()))
                    .toList();
            System.out.println("All action IDs in backend:");
            actions.forEach(a -> System.out.println(a.getId()));


            System.out.println("Filtered by action IDs count: " + actions.size());
        } else if (request.getRows() != null && request.getRows() > 0) {
            actions = actions.stream()
                    .limit(request.getRows())
                    .toList();
        } else {
            throw new IllegalArgumentException("No actions selected for export!");
        }


        ByteArrayInputStream in;
        if ("pdf".equalsIgnoreCase(format)) {
            in = userActionService.generatePdfReport(actions);
        } else {
            in = userActionService.generateExcelReport(actions);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=UserActions." +
                ("pdf".equalsIgnoreCase(format) ? "pdf" : "xlsx"));

        return ResponseEntity.ok()
                .headers(headers)
                .contentType("pdf".equalsIgnoreCase(format) ?
                        MediaType.APPLICATION_PDF : MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(in));
    }

    @PostMapping("/export/option")
    public ResponseEntity<byte[]> exportUsers(@RequestBody UserExportRequest request, @AuthenticationPrincipal User user) throws Exception {

        // Fetch filtered users based on selected IDs, role, and limit
        List<User> users = userService.getUsersForExport(
                request.getIds(), request.getRole(), request.getLimit()
        );

        ByteArrayInputStream bis;
        String filename;

        if ("pdf".equalsIgnoreCase(request.getFormat())) {
            bis = userService.generatePdfReport(users);
            filename = "users.pdf";
        } else {
            bis = userService.generateExcelReport(users);
            filename = "users.xlsx";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + filename);


        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bis.readAllBytes());
    }


}
