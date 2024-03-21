package rocky.com.example.virtualbookstore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rocky.com.example.virtualbookstore.module.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface userRepository extends CrudRepository<User,Integer> {

    //for return the userdetails, utilize Optional package User make collection nullable avoid send nullpointerexception
    Optional<User> findByEmail(String email);
}
