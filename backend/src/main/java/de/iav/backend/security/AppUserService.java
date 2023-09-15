package de.iav.backend.security;

import de.iav.backend.model.User;
import de.iav.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    //public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //AppUser appUser = appUserRepository.findAppUserByUsername(username)
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


    }

    public AppUserResponse register(NewAppUser newAppUser) {

        Argon2PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

        User user = new User(
                null,
                newAppUser.firstName(),
                newAppUser.lastName(),
                newAppUser.email(),
                passwordEncoder.encode(newAppUser.password()),
                "USER"
        );
        User savedUser = userRepository.save(user);
        return new AppUserResponse(savedUser.getId(),
                savedUser.getFirstName(), savedUser.getLastName(),
                savedUser.getEmail(), savedUser.getRole());
    }
}