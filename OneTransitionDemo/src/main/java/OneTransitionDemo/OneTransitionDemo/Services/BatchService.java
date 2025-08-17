package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Models.Batch;
import OneTransitionDemo.OneTransitionDemo.Repositories.BatchRepository;
import OneTransitionDemo.OneTransitionDemo.Request.BatchRequest;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class BatchService {

    @Autowired
    private BatchRepository  batchRepository;

    public Map<String, Object> getAllBatch(){
        return ResponseUtil.success("batch found!", batchRepository.findAll());
    }

    public Map<String, Object> createBatch(BatchRequest request){
        Batch batch = new Batch();
        batch.setEndYear(request.getEndYear());
        batch.setStartYear(request.getStartYear());
        batchRepository.save(batch);
        return ResponseUtil.success("batch created successfully!");
    }
    public Map<String, Object> updateBatch(BatchRequest request){
        Optional <Batch> batchOptional = batchRepository.findById(request.getId());
        if(batchOptional.isEmpty()) return ResponseUtil.error("batch not found!");
        Batch batch = batchOptional.get();
        batch.setEndYear(request.getEndYear());
        batch.setStartYear(request.getStartYear());
        batchRepository.save(batch);
        return ResponseUtil.success("batch updated successfully!");
    }
    public Map<String, Object> deleteBatch(Long id){
        Optional <Batch> batchOptional = batchRepository.findById(id);
        if(batchOptional.isEmpty()) return ResponseUtil.error("batch not found!");
        Batch batch = batchOptional.get();
        batchRepository.delete(batch);
        return ResponseUtil.success("batch deleted successfully!");
    }
}
