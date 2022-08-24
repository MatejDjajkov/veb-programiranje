package mk.ukim.finki.wp.exam.example.service.impl;

import mk.ukim.finki.wp.exam.example.model.Role;
import mk.ukim.finki.wp.exam.example.model.User;
import mk.ukim.finki.wp.exam.example.repository.UserRepository;
import mk.ukim.finki.wp.exam.example.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl  implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(String username, String password, Role role) {
        return this.userRepository.save(new User(username,passwordEncoder.encode(password),role));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user=this.userRepository.findByUsername(username)
               .orElseThrow(()-> new UsernameNotFoundException(username));
       UserDetails userDetails=new org.springframework.security.core.userdetails.User
               (user.getUsername()
               ,user.getPassword()
                , Stream.of(new SimpleGrantedAuthority(user.getRole().toString())).collect(Collectors.toList()));
       return userDetails;
    }
}
