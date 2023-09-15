package de.iav.backend.repository;


import de.iav.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

    Optional<User> findUserByEmail(String userEmail);

}