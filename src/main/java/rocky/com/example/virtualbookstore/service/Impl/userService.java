package rocky.com.example.virtualbookstore.service.Impl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import rocky.com.example.virtualbookstore.module.User;
import rocky.com.example.virtualbookstore.module.UserInfoDetails;
import rocky.com.example.virtualbookstore.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import rocky.com.example.virtualbookstore.request.registerRequest;

import java.util.Date;
import java.util.Optional;

@Component
public class userService implements UserDetailsService {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByEmail(username);
        return userDetail.map(UserInfoDetails::new).orElseThrow(()->new UsernameNotFoundException("username Invalid"));
    }

    public User addUser(registerRequest registerqequest){
       User user = new User();
        user.setEmail(registerqequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerqequest.getPassword()));
        user.setRole(registerqequest.getRole());
        Date now = new Date();
        user.setCreateDate(now);
        user.setLastModifyDate(now);
        userRepository.save(user);
        return  user;
    }
}
