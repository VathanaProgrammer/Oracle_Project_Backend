package OneTransitionDemo.OneTransitionDemo.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class AdminDepartmentRequest {
    @JsonProperty("teacherId")
    private Long userId;
    private String name;
    private Long majorId;

    public Long getUserId() {
        return userId;
    }

    public Long getMajorId() {
        return majorId;
    }

    public String getName() {
        return name;
    }
}
