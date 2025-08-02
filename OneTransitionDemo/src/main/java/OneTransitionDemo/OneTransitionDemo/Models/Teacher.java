package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table( name = "TBL_TEACHERS")
public class Teacher {
    @Id
<<<<<<< HEAD
    @GeneratedValue(strategy = GenerationType.IDENTITY)
=======
    @GeneratedValue (strategy = GenerationType.IDENTITY)
>>>>>>> e99ab82641a5f2978ec2c7811e3d8817fc3ffdc6
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "teacher")
    private List<Exam> examsCreated;

    @ManyToMany(mappedBy = "teachers")
    private Set<Department> departments = new HashSet<>();

    @Version
    private Integer version;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Set<Department> getDepartments() { return departments; }
    public void setDepartments(Set<Department> departments) { this.departments = departments; }

    public void addDepartment(Department department) {
        this.departments.add(department);
        department.getTeachers().add(this);
    }

}

