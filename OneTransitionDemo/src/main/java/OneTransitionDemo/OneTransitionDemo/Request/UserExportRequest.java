package OneTransitionDemo.OneTransitionDemo.Request;

import java.util.List;

public class UserExportRequest {
    private List<Long> ids;       // selected users
    private String role;          // ALL, ADMIN, TEACHER, STUDENT
    private Integer limit;        // limit number
    private String format;        // pdf or excel

    // getters and setters


    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
