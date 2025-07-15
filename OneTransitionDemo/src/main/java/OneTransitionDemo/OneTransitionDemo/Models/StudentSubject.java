package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_student_subjects")
public class StudentSubject {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
