package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

@Entity
@Table( name = "TBL_TEACHERS")
public class Teacher {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToOne
    private User user;

    @ManyToOne
    private Department department;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}

