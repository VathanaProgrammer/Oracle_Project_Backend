package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Repositories.BatchRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BatchService {

    @Autowired
    private BatchRepository  batchRepository;

    public Map<String, Object> getAllBatch(){
        return ResponseUtil.success("batch found!", batchRepository.findAll());
    }
}
