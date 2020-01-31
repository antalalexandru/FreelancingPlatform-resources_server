package resources.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@Entity
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;

    @Column
    @ToString.Exclude
    @JsonProperty(access = WRITE_ONLY)
    private String password;

    @Column
    @ToString.Exclude
    @JsonProperty(access = WRITE_ONLY)
    private String email;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column
    @ToString.Exclude
    @JsonProperty(access = WRITE_ONLY)
    private boolean is_enabled;
}
