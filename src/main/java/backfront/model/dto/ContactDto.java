package backfront.model.dto;

import lombok.*;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ContactDto {
    private String fullName;

    private String email;

    private List<String> phoneNumbers;

    private List <AddressDto> addresses;

}
