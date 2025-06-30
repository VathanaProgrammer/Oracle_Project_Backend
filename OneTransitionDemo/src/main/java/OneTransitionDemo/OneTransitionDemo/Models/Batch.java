package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table ( name = "TBL_BATCH")
public class Batch {
    @Id
    @GeneratedValue
    private Long id;
    private String year;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
}