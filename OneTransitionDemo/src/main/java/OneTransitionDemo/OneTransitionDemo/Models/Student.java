package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

@Entity
@Table( name = "TBL_STUDENTS")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Academic info (linked entities)
    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;          // e.g. 2025 Intake

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;    // e.g. Semester 1

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;          // Morning / Evening

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;          // e.g. Computer Science

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;  // e.g. Room A-101

    // Other info
    private Long year;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Major getMajor() { return major; }
    public void setMajor(Major major) { this.major = major; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }
    public Long getYear() { return year; }
    public void setYear(Long year) { this.year = year; }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public Shift getShift() { return shift; }
    public void setShift(Shift shift) { this.shift = shift; }
}
