package rocky.com.example.virtualbookstore.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import rocky.com.example.virtualbookstore.module.User;
import rocky.com.example.virtualbookstore.request.authenticationRequest;
import rocky.com.example.virtualbookstore.request.registerRequest;
import rocky.com.example.virtualbookstore.request.userSearchRequest;
import rocky.com.example.virtualbookstore.request.userUpdateRequest;

import java.util.Date;
import java.util.List;

public interface userService  {


    User findUserByEmail(String username);

    User addUser(registerRequest registerqequest);

    User modifyUserAuthority(String username);

    void deleteUser(String username);

    Date updateUserPassword(userUpdateRequest userUpdateRequest);

    Page<User> findAllUser (userSearchRequest userSearchRequest);
}
