package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table( name = "TBL_TEACHERS")
public class Teacher {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "teacher")
    private List<Exam> examsCreated;

    @ManyToOne
    private Department department;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}

