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

    private String username;

    @ToString.Exclude
    @JsonProperty(access = WRITE_ONLY)
    private String password;

    @ToString.Exclude
    @JsonProperty(access = WRITE_ONLY)
    private String email;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ToString.Exclude
    @JsonProperty(access = WRITE_ONLY)
    private boolean is_enabled;
}
