package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Request.BatchRequest;
import OneTransitionDemo.OneTransitionDemo.Services.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<?> registerNewBatch(@AuthenticationPrincipal User user,@RequestBody BatchRequest request){
        Map<String , Object> res = batchService.createBatch(request);
        return ResponseEntity.status((Boolean) res.get("success") ? 200 : 400).body(res);
    }

    @PutMapping
    public ResponseEntity<?> updateBatch(@AuthenticationPrincipal User user,@RequestBody BatchRequest request){
        System.out.println("update batch id: " + request.getId());
        Map<String, Object> res = batchService.updateBatch(request);
        return ResponseEntity.status((Boolean) res.get("success") ? 200 : 400).body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBatch(@AuthenticationPrincipal User user, @PathVariable Long id){
        Map<String, Object> response = batchService.deleteBatch(id);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }
}
