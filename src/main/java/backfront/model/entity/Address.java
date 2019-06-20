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
@Getter
@Setter
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

    @Past
   private LocalDateTime DateTime;
    @NotBlank
   private  String country;
    @NotBlank
   private String city;
    @NotBlank
   private String street;
    @NotBlank
   private String houseNumber;
    @NotBlank
   private String apartment;
    @OneToMany
    private List<Users> user;


}
