package OneTransitionDemo.OneTransitionDemo.Request;

import java.util.List;

public class ExportRequest {
    private List<Long> ids; // selected rows
    private Integer rows;   // 10,20,50,1000
    private boolean all;    // true if "All" selected

    // getters and setters


    public boolean isAll() {
        return all;
    }

    public Integer getRows() {
        return rows;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
