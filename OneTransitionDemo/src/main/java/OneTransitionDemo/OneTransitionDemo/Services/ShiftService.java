package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Repositories.ShiftRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    public Map<String, Object> getAllShifts(){
        return ResponseUtil.success("All Shifts found!", shiftRepository.findAll());
    }
}
