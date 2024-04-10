package rocky.com.example.virtualbookstore.module;

import jakarta.persistence.*;
import lombok.Data;
import rocky.com.example.virtualbookstore.constant.bookCategory;

import java.util.Date;

@Entity(name = "book")
@Table(name = "book")
@Data
public class bookstore_book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookid;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "author")
    private String author;

    @Column(name = "price")
    private Integer price;

    @Column(name = "description")
    private String description;

    @Column(name = "catagory")
    @Enumerated(EnumType.STRING)
    private bookCategory category;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "language")
    private String language;

    @Column(name = "publication_date")
    private Date publicationDate;

}
