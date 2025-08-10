package OneTransitionDemo.OneTransitionDemo.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table( name = "TBL_MAJORS")
public class Major {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Major(){}

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JsonBackReference
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}

