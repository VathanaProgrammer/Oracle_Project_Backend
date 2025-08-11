package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Repositories.LocationRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Transactional
public class LocationService {

    @Autowired
    private LocationRepository locationRepo;

    public Map<String, Object> getAllLocations() {
        return ResponseUtil.success("Location found!", locationRepo.findAll());
    }
}
