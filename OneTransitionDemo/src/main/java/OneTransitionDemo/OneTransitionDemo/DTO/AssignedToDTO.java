package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.AssignedTo;

public class AssignedToDTO {
    private Long id;
    private int batch;
    private int year;
    private String location;
    private String shiftName;
    private String shiftTime;
    private String major;
    private String subjectName;


    public AssignedToDTO() {}

    public AssignedToDTO(AssignedTo assignedTo) {
        this.id = assignedTo.getId();
        this.shiftName = assignedTo.getShiftName();
        this.shiftTime = assignedTo.getShiftTime();
        this.location = assignedTo.getLocation();
        this.batch = assignedTo.getBatch();
        this.year = assignedTo.getYear();
        this.major = assignedTo.getMajor() != null ? assignedTo.getMajor().getName() : null;
        this.subjectName = assignedTo.getSubject().getName() != null ? assignedTo.getSubject().getName() : null;
    }
    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public int getBatch() {
        return batch;
    }
    public void setBatch(int batch) {
        this.batch = batch;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getShiftName() {
        return shiftName;
    }
    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getShiftTime() {
        return shiftTime;
    }
    public void setShiftTime(String shiftTime) {
        this.shiftTime = shiftTime;
    }

    public String getMajor() {
        return major;
    }
    public void setMajor(String major) {
        this.major = major;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
