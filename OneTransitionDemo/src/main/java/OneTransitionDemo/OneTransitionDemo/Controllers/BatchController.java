package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Services.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/batchs")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @GetMapping
    private ResponseEntity<?> getAllBatch(){
        Map<String,Object> map = batchService.getAllBatch();
        return ResponseEntity.status((Boolean) map.get("success") ? 200 : 400).body(map);
    }
}
