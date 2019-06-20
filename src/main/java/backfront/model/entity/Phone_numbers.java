package backfront.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Phone_numbers {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Past
    private LocalDateTime localDateTime;
    @NotBlank
    @Column(unique = true)
    private String phoneNumber;
    @OneToMany
    private List<Users> user;

}
