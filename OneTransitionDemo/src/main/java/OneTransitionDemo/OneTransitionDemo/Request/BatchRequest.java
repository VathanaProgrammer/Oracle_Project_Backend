package OneTransitionDemo.OneTransitionDemo.Request;

public class BatchRequest {
    private Long id;
    private int startYear;
    private int endYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getStartYear() {
        return startYear;
    }
}
