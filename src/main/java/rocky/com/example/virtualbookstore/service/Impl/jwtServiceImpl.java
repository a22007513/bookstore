package rocky.com.example.virtualbookstore.service.Impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import rocky.com.example.virtualbookstore.constant.Role;
import rocky.com.example.virtualbookstore.module.User;
import rocky.com.example.virtualbookstore.repository.userRepository;
import rocky.com.example.virtualbookstore.service.jwtService;

import java.security.Key;
import java.util.*;

@Component
@Slf4j
public class jwtServiceImpl implements jwtService {

    //cause the jwt algorithm we generate random 256 bits Hex value
    private static final String SECRET_KEY="565d35274427565f354c3c7c284f58375a7a3d35725020507835355768";
    @Autowired
    userRepository userrepository;

    public User getUserByEmail(String email) {
        //if query from DB by email not null return object else return exception through Optional property
        User user = userrepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User name not found"));
        return user;
    }


    public void deleteUserByid(Integer userid) {
        userrepository.deleteById(userid);
    }


    public User updateUser(String username,String authorize) {
            User user = userrepository.findByEmail(username).orElse(null);
            String role = authorize;
            user.setRole(role);
            System.out.println(user);
            userrepository.save(user);
            //List<User> updateUser = getUserByEmail(user.getEmail());
            return user;
    }


    public User createUser(User user) {
        userrepository.save(user);
        User newUser = getUserByEmail(user.getEmail());
        return newUser;
    }

    public String extractUserName(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    //check token expired date before current time
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public Claims extractAllClaim(String token) {
        return Jwts
                // the signinkey for generate jwt signature part verify owner of signinkey and send jwtkey is same
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //extract all claim to String
    public <T> T extractClaim(String token, Function<Claims,T> claimResolve){
        Claims claims = extractAllClaim(token);
        //claimResolve function for convert Claims => T
        return claimResolve.apply(claims);
    }

    //encrypt the self define SECRET KEY with base64 as signinkey
    public Key getSignInKey(){
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }
    public String createToken(String userName){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userName);
    }

    //bring the signinkey to generate token
    public String createToken(Map<String,Object> extraClaims,
                                String userName
    ){

       return Jwts.builder()
               //claim is null able
               .setClaims(extraClaims)
               //.claim("authorities",authList)
               .setSubject(userName)
               //jwt issue time
               .setIssuedAt(new Date(System.currentTimeMillis()))
               //jwt expired time
               .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
               .signWith(getSignInKey(), SignatureAlgorithm.HS256)
               .compact();
    }

    //check the username from userdetail and from claim of token is same and the expired date
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username = extractUserName(token);
        log.info("user name from UserDetail{}",userDetails.getUsername());
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
