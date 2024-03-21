package rocky.com.example.virtualbookstore.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class registerRequest {
    @Email
    private String email;
    private String password;
    private String role;
}
