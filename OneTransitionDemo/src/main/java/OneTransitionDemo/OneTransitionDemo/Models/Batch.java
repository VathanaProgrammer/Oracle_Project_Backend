package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;
@Entity
@Table(name = "TBL_BATCHES")
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int startYear;  // e.g., 2022
    private int endYear;    // e.g., 2026 (startYear + 4)

    // You can optionally add a computed property or method for "batchName" like "2022-2026"

    // Constructors, getters, setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getStartYear() { return startYear; }
    public void setStartYear(int startYear) { this.startYear = startYear; }

    public int getEndYear() { return endYear; }
    public void setEndYear(int endYear) { this.endYear = endYear; }

    public String getBatchName() {
        return startYear + "-" + endYear;
    }
}
