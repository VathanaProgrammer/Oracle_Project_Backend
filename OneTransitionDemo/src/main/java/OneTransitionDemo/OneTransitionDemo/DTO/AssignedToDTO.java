package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.AssignedTo;

public class AssignedToDTO {
    private Long id;
    private String batch;
    private Long batchId;
    private Long semesterId;
    private Long semesterNumber;
    private Long shiftId;
    private Long subjectId;
    private Long locationId;
    private String location;
    private String buildingName;
    private String floorNumber;
    private Long majorId;
    private Long academicYearId;
    private String year;
    private String shiftName;
    private String shiftTime;
    private String major;
    private String subjectName;


    public AssignedToDTO() {}

    public AssignedToDTO(AssignedTo assignedTo) {
        this.id = assignedTo.getId();

        this.shiftId = assignedTo.getShift().getId();
        this.shiftName = assignedTo.getShift().getName() != null ? assignedTo.getShift().getName() : null;
        this.shiftTime = assignedTo.getShift().getStartTime() + "-" + assignedTo.getShift().getEndTime();

        this.buildingName = assignedTo.getLocation().getBuildingName();
        this.floorNumber = assignedTo.getLocation().getFloorNumber();
        this.locationId = assignedTo.getLocation().getId();
        this.location = assignedTo.getLocation().getRomeName();

        this.batchId = assignedTo.getBatch().getId();
        this.batch = assignedTo.getBatch().getStartYear() + "-"+  assignedTo.getBatch().getEndYear();

        this.academicYearId = assignedTo.getAcademicYear().getId();
        this.year = assignedTo.getAcademicYear().getName();

        this.semesterId = assignedTo.getSemester().getId();
        this.semesterNumber = assignedTo.getSemester().getNumber();
        this.subjectId = assignedTo.getSubject().getId();

        this.majorId = assignedTo.getMajor().getId();
        this.major = assignedTo.getMajor() != null ? assignedTo.getMajor().getName() : null;

        this.semesterId = assignedTo.getSemester().getId();
        this.subjectName = assignedTo.getSubject().getName() != null ? assignedTo.getSubject().getName() : null;
    }
    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getBatch() {
        return batch;
    }
    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getYear() {
        return year;
    }
    public void setYear(String year) {
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
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getSemesterNumber() {
        return semesterNumber;
    }

    public Long getLocationId() {
        return locationId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public Long getMajorId() {
        return majorId;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public Long getAcademicYearId() {
        return academicYearId;
    }
}
