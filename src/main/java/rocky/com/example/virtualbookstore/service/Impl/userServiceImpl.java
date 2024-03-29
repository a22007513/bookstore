package rocky.com.example.virtualbookstore.service.Impl;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import rocky.com.example.virtualbookstore.constant.Role;
import rocky.com.example.virtualbookstore.module.User;
import rocky.com.example.virtualbookstore.module.UserInfoDetails;
import rocky.com.example.virtualbookstore.repository.userCriteriaQuery;
import rocky.com.example.virtualbookstore.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import rocky.com.example.virtualbookstore.request.authenticationRequest;
import rocky.com.example.virtualbookstore.request.registerRequest;
import rocky.com.example.virtualbookstore.request.userSearchRequest;
import rocky.com.example.virtualbookstore.request.userUpdateRequest;
import rocky.com.example.virtualbookstore.service.userService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component("GetUserDetail")
public class userServiceImpl implements userService, UserDetailsService {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private userCriteriaQuery userCriteriaQuery;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByEmail(username);
        return userDetail.map(UserInfoDetails::new).orElseThrow(()->new UsernameNotFoundException("username Invalid"));
    }

    public User findUserByEmail(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("username Invalid"));
        return user ;
    }

    public User addUser(registerRequest registerqequest){
       User user = new User();
        user.setEmail(registerqequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerqequest.getPassword()));
        Role role = Role.valueOf(registerqequest.getRole());
        user.setRole(role);
        Date now = new Date();
        user.setCreateDate(now);
        user.setLastModifyDate(now);
        userRepository.save(user);
        return  user;
    }

    public User modifyUserAuthority(String username){
        User user = userRepository.findByEmail(username).orElse(null);
        user.setRole(Role.ROLE_ADMIN);
        Date now = new Date();
        user.setLastModifyDate(now);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public void deleteUser(String username){
        userRepository.deleteByEmail(username);
    }

    public Page<User> findAllUser(userSearchRequest userSearchRequest){
        Page<User> result =  userCriteriaQuery.findAll(userSearchRequest);
        return result;
    }

    public Date updateUserPassword(userUpdateRequest userUpdateRequest){
        User user = userRepository.findByEmail(userUpdateRequest.getEmail()).orElseThrow(
                () ->   new UsernameNotFoundException("Invalid username"));
        if(!passwordEncoder.matches(userUpdateRequest.getPassword(), user.getPassword())){
            throw new UsernameNotFoundException("User Password Wrong");
        }
        user.setPassword(passwordEncoder.encode(userUpdateRequest.getUpdatePassword()));
        Date now = new Date();
        user.setLastModifyDate(now);
        userRepository.save(user);
        return now;
    }
}
