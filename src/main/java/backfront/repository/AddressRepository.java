package backfront.repository;

import backfront.model.entity.Address;
import backfront.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressRepository,Long> {

    List<Address> findAllByUser(Users user);

    void deleteAllByUser(Users user);

    Optional<Address> findBy(String country,
                             String city,
                             String street,
                             String houseNumber,
                             String apartment);

}

