package resources.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name = "applications")
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Currently we only use to retrieve applications related to a specific project;
     * No need to map @ManyToOne here :).
     */
    @Column
    private long projectId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String description;

    @Column
    private Date date;
}
