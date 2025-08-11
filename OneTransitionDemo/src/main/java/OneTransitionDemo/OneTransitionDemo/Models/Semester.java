package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "TBL_SEMESTERS")
public class Semester {
    @Id
    @SequenceGenerator(name = "semester_seq", sequenceName = "SEMESTER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "semester_seq")
    private Long id;

    @Column(name = "semester_number")
    private Long number; // 1 or 2

    // Constructors, getters, setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getNumber() { return number; }
    public void setNumber(Long number) { this.number = number; }
}

