package de.iav.backend.repository;


import de.iav.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findUserByEmail(String userEmail);
    List<User> findAllByFirstNameEqualsIgnoreCase(String firstName);
    List<User> findAllByLastNameEqualsIgnoreCase(String lastName);
    List<User> findAllByEmailEqualsIgnoreCase(String email);
    List<User> findAllByRoleEqualsIgnoreCase(String role);

    Optional<User> findUserByFirstNameEqualsIgnoreCaseAndLastNameEqualsIgnoreCase(String firstName, String lastName);

    Optional<User> findUserByFirstNameEqualsIgnoreCaseAndEmailEqualsIgnoreCase(String firstName, String email);
}