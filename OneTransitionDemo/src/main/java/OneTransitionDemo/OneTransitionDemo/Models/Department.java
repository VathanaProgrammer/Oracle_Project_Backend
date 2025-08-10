package OneTransitionDemo.OneTransitionDemo.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "TBL_DEPARTMENTS")
public class Department {
    public Department() {}

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean isDeleted;

    @ManyToMany
    @JsonBackReference
    @JoinTable(
            name = "TBL_DEPARTMENT_TEACHERS",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Major> majors = new HashSet<>();
    public Set<Major> getMajors() {
        return majors;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setMajors(Set<Major> majors) {
        this.majors = majors;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Teacher> getTeachers() { return teachers; }
    public void setTeachers(Set<Teacher> teachers) { this.teachers = teachers; }
}
