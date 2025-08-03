package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

@Entity
@Table( name = "TBL_STUDENTS")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private Long year;

    private Long batch;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;


    @ManyToOne
    private ClassGroup classGroup;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Major getMajor() { return major; }
    public void setMajor(Major major) { this.major = major; }

    public ClassGroup getClassGroup() { return classGroup; }
    public void setClassGroup(ClassGroup classGroup) { this.classGroup = classGroup; }

    public Long getBatch() { return batch; }
    public void setBatch(Long batch) { this.batch = batch; }
    public Long getYear() { return year; }
    public void setYear(Long year) { this.year = year; }
}
