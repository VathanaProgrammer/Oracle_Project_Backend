package OneTransitionDemo.OneTransitionDemo.DTO;

public class DepartmentSummaryDTO {
    private Long id;
    private String name;
    private Long teacherCount;
    private Long majorCount;

    public DepartmentSummaryDTO() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getTeacherCount() {
        return teacherCount;
    }
    public void setTeacherCount(Long teacherCount) {
        this.teacherCount = teacherCount;
    }

    public Long getMajorCount() {
        return majorCount;
    }
    public void setMajorCount(Long majorCount) {
        this.majorCount = majorCount;
    }
}
