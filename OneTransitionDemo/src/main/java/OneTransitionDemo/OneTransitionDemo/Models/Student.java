package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

@Entity
@Table( name = "TBL_STUDENTS")
public class Student {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToOne
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;


    @ManyToOne
    private ClassGroup classGroup;

    @ManyToOne
    private Batch batch;

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

    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }
}
