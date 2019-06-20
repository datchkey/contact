package backfront.repository;

import backfront.model.entity.Phone_numbers;
import backfront.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhoneNumberRepository extends JpaRepository<Phone_numbers,Long> {

    List<Phone_numbers> findAllByUser(Users user);

    Optional<Phone_numbers> findPhoneNumberByPhoneNumber(String phoneNumber);

    Optional <List <Phone_numbers>> findAllByPhoneNumber(String phoneNumber);

    void deleteAllByUser(Users user);

    boolean existsByPhoneNumber(String phoneNumber);
}
