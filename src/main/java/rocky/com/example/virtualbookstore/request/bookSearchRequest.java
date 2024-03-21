package rocky.com.example.virtualbookstore.request;

import lombok.Data;
import org.springframework.data.domain.Sort;
import rocky.com.example.virtualbookstore.constant.bookCategory;

@Data
public class bookSearchRequest {
    private bookCategory categeory;
    private String bookName;
    private Integer PageSize;
    private Integer PageNumber;
    private String orderBy;
    private Sort.Direction sortMethod;
}
