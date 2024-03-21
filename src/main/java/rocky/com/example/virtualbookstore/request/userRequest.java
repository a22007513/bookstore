package rocky.com.example.virtualbookstore.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class userRequest {
    @Email
    private String email;
}
