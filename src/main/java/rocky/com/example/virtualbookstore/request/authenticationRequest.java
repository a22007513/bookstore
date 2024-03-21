package rocky.com.example.virtualbookstore.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class authenticationRequest {
    @Email
    private String email;
    private String password;
}
