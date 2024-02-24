package rocky.com.example.virtualbookstore.module;

import jakarta.persistence.*;
import lombok.Data;
import rocky.com.example.virtualbookstore.util.bookCategory;

import java.util.Date;

@Entity
@Table(name = "book")
@Data
public class bookstore_book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    Integer bookid;

    @Column(name = "book_name")
    String bookName;

    @Column(name = "author")
    String author;

    @Column(name = "price")
    Integer price;

    @Column(name = "description")
    String description;

    @Column(name = "catagory")
    @Enumerated(EnumType.STRING)
    bookCategory category;

    @Column(name = "stock")
    Integer stock;

    @Column(name = "language")
    String language;

    @Column(name = "publication_date")
    Date publicationDate;
}
