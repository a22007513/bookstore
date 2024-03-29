package rocky.com.example.virtualbookstore.request;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.Date;

@Data
public class userSearchRequest {

    private String search;
    private Integer limit;
    private Integer offset;
    private String orderBy;
    private Sort.Direction sortMethod;
}
