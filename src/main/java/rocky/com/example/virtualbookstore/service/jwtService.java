package rocky.com.example.virtualbookstore.service;

import org.springframework.security.core.userdetails.UserDetails;
import rocky.com.example.virtualbookstore.module.User;

import java.util.Map;

public interface jwtService {
    
  User getUserByEmail(String email);

    void deleteUserByid(Integer userid);

    User updateUser(String username,String authorize);


    String extractUserName(String token);


   String createToken(String username);

   String createToken(Map<String,Object> extraClaims,
                      String userName);

  boolean isTokenValid(String token,UserDetails userDetails);
}
