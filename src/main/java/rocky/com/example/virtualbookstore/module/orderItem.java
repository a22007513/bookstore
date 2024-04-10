package rocky.com.example.virtualbookstore.module;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "orderitem")
public class orderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderitemid")
    private Integer orderitemId;

    @Column(name = "bookid")
    private Integer bookid;

    @Column(name = "orderid")
    private Integer orderId;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "quantity")
    private Integer quantity;

    @OneToMany()
    private List<bookstore_book> books;
}
