package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "TBL_TEACHING_ASSIGNMENTS")
public class TeachingAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private AssignedTo assignedTo;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    public AssignedTo getAssignedTo() {
        return this.assignedTo;
    }
    public void setAssignedTo(AssignedTo assignedTo) {
        this.assignedTo = assignedTo;
    }
    public User getTeacher() {
        return this.teacher;
    }
    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}

