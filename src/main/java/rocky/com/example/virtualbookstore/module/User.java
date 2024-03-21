package rocky.com.example.virtualbookstore.module;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rocky.com.example.virtualbookstore.constant.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User  {

    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Integer userid;

    @Column(name="email")
    String email;

    @Column(name="passwd")
    String password;

    @Column(name="create_date")
    Date createDate;

    @Column(name="last_modify_date")
    Date lastModifyDate;

    //role for granted user authority
    @Column(name="role")
    //@Enumerated(EnumType.STRING)
    String role;

}
