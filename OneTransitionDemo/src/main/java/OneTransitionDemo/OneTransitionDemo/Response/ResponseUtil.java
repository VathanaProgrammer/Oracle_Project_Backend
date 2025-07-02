package OneTransitionDemo.OneTransitionDemo.Response;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    public static Map<String, Object> success(String message) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", message);
        return res;
    }

    public static <T> Map<String, Object> success(String message, T data) {
        Map<String, Object> res = success(message);
        res.put("data", data);
        return res;
    }

    public static Map<String, Object> error(String message) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", false);
        res.put("message", message);
        return res;
    }
}
