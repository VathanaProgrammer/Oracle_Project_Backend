package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;
<<<<<<< HEAD

import java.util.HashSet;
import java.util.Set;
=======
>>>>>>> e99ab82641a5f2978ec2c7811e3d8817fc3ffdc6

@Entity
@Table( name = "TBL_DEPARTMENTS")
public class Department {
    public Department() {}

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(
            name = "TBL_DEPARTMENT_TEACHERS",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<Teacher> teachers = new HashSet<>();
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Teacher> getTeachers() { return teachers; }
    public void setTeachers(Set<Teacher> teachers) { this.teachers = teachers; }
}
