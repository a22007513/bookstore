package rocky.com.example.virtualbookstore.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class userUpdateRequest {

    @Email
    private String email;
    private String password;
    private String updatePassword;
}
