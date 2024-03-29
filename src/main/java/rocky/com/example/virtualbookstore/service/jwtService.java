package rocky.com.example.virtualbookstore.service;

import org.springframework.security.core.userdetails.UserDetails;
import rocky.com.example.virtualbookstore.module.User;

import java.util.Map;

public interface jwtService {

    String extractUserName(String token);


   String createToken(String username);

   String createToken(Map<String,Object> extraClaims,
                      String userName);

  boolean isTokenValid(String token,UserDetails userDetails);
}
