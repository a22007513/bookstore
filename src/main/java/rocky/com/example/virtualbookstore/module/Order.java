package rocky.com.example.virtualbookstore.module;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid")
    Integer orderId;

    @Column(name = "userid")
    Integer userid;

    @Column(name = "totalamount")
    Integer totalAmount;

    @Column(name = "totalquantity")
    Integer totalQuantity;

    @Column(name = "createdate")
    Date createDate;

    @Column(name = "lastmodifydate")
    Date lastModifyDate;

}
