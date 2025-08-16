package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.UserActionDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.UserActionReportDTO;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import OneTransitionDemo.OneTransitionDemo.Models.UserAction;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserActionRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class UserActionService {
    private static final Logger log = LoggerFactory.getLogger(UserActionService.class);
    @Autowired
    private UserActionRepository userActionRepository;

    public Map<String, Object> recordAction(Long userId, String type, String label, String description, String firstname, String lastname, String profilePicUrl, Role role) {
        UserAction action = new UserAction();
        action.setUserId(userId);
        action.setFirstName(firstname);
        action.setLastName(lastname);
        action.setType(type);
        action.setLabel(label);
        action.setDescription(description);
        action.setTimestamp(LocalDateTime.now());
        action.setProfilePicture(profilePicUrl);
        action.setRole(role);
        userActionRepository.save(action);
        return ResponseUtil.success("Action recorded successfully!");
    }

    public Map<String, Object> getUserRecentActionsById(Long userId){
        List<UserActionDTO> userActionDTOS = userActionRepository.findByUserId(userId)
                .stream()
                .map(action -> {
                    UserActionDTO dto = new UserActionDTO();
                    dto.setId(action.getId());
                    dto.setUserId(action.getUserId());
                    dto.setDescription(action.getDescription());
                    dto.setFirstName(action.getFirstName());
                    dto.setLastName(action.getLastName());
                    dto.setType(action.getType());
                    dto.setTimestamp(action.getTimestamp());
                    return dto;
                })
                .toList();

        return ResponseUtil.success("Recent actions found!", userActionDTOS);
    }

    public Map<String, Object> getAllUserAction(){
        List<UserActionDTO> userActionDTOS = userActionRepository.findAll()
                .stream()
                .map(action -> {
                    UserActionDTO dto = new UserActionDTO();
                    dto.setId(action.getId());
                    dto.setUserId(action.getUserId());
                    dto.setDescription(action.getDescription());
                    dto.setFirstName(action.getFirstName());
                    dto.setLastName(action.getLastName());
                    dto.setRole(action.getRole());
                    dto.setProfilePicture(action.getProfilePicture());
                    dto.setType(action.getType());
                    dto.setTimestamp(action.getTimestamp());
                    return dto;
                })
                .toList();

        return ResponseUtil.success("actions found!", userActionDTOS);
    }

    public ByteArrayInputStream generateExcelReport(List<UserActionReportDTO> actions) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("User Actions");

            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"User ID", "Full Name", "Role", "Action Type", "Description", "Timestamp", "Browser", "Device Type", "Device", "Location"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Date format for Excel cells
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss a"));

            // Data rows
            int rowIdx = 1;
            for (UserActionReportDTO action : actions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(action.getUserId() != null ? action.getUserId() : 0); // NEW: User ID
                row.createCell(1).setCellValue(action.getFullName() != null ? action.getFullName() : "");
                row.createCell(2).setCellValue(action.getRole() != null ? action.getRole().name() : "");
                row.createCell(3).setCellValue(action.getActionType() != null ? action.getActionType() : "");
                row.createCell(4).setCellValue(action.getDescription() != null ? action.getDescription() : "");

                // Format timestamp cell
                if (action.getTimestamp() != null) {
                    Cell dateCell = row.createCell(5);
                    dateCell.setCellValue(java.sql.Timestamp.valueOf(action.getTimestamp())); // convert LocalDateTime
                    dateCell.setCellStyle(dateCellStyle);
                } else {
                    row.createCell(5).setCellValue("");
                }

                row.createCell(6).setCellValue(action.getBrowser() != null ? action.getBrowser() : "");
                row.createCell(7).setCellValue(action.getDeviceType() != null ? action.getDeviceType() : "");
                row.createCell(8).setCellValue(action.getDevice() != null ? action.getDevice() : "");
                row.createCell(9).setCellValue(action.getLocation() != null ? action.getLocation() : "");
            }

            // Auto-size columns for better readability
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream generatePdfReport(List<UserActionReportDTO> actions) throws Exception {
        Document document = new Document(PageSize.A4.rotate()); // landscape for more width
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("User Actions Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Table with 10 columns (User ID, Full Name, Role, Action Type, Description, Timestamp, Browser, Device Type, Device, Location)
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 4, 2, 3, 5, 3, 3, 3, 3, 4}); // adjust widths

        // Header row
        String[] headers = {"User ID", "Full Name", "Role", "Action Type", "Description",
                "Timestamp", "Browser", "Device Type", "Device", "Location"};
        for (String header : headers) {
            table.addCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        }

        // Timestamp formatter
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");

        // Data rows
        for (UserActionReportDTO action : actions) {
            table.addCell(action.getUserId() != null ? String.valueOf(action.getUserId()) : "");
            table.addCell(action.getFullName() != null ? action.getFullName() : "");
            table.addCell(action.getRole() != null ? action.getRole().name() : "");
            table.addCell(action.getActionType() != null ? action.getActionType() : "");
            table.addCell(action.getDescription() != null ? action.getDescription() : "");
            table.addCell(action.getTimestamp() != null ? action.getTimestamp().format(dtf) : "");
            table.addCell(action.getBrowser() != null ? action.getBrowser() : "");
            table.addCell(action.getDeviceType() != null ? action.getDeviceType() : "");
            table.addCell(action.getDevice() != null ? action.getDevice() : "");
            table.addCell(action.getLocation() != null ? action.getLocation() : "");
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
