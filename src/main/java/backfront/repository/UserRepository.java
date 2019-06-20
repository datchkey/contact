package backfront.repository;


import backfront.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<Users,Long> {
    @Query("select u.id from Users u")                  //????????????
    List<Long> getAllIds();

    Users findUserByFullName(String fullName);

}
