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
}

