package backfront.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhoneNumberNotFoundException extends RuntimeException {

    public PhoneNumberNotFoundException(String message) {
        super(message);
    }
}