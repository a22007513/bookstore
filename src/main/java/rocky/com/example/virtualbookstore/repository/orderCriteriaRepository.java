package rocky.com.example.virtualbookstore.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import rocky.com.example.virtualbookstore.module.bookstore_book;
import rocky.com.example.virtualbookstore.module.orderItem;

import java.util.ArrayList;
import java.util.List;

@Repository
public class orderCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public orderCriteriaRepository(EntityManager entityManager){
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public List<orderItem> findOrderItemTest(){
        CriteriaQuery<orderItem> query = criteriaBuilder.createQuery(orderItem.class);
        Root<orderItem> root = query.from(orderItem.class);
        Join<orderItem,bookstore_book> orderJioin =  root.join("books");
        query.multiselect(
                orderJioin.get("bookid"));
//        TypedQuery<orderItem>typedQuery = entityManager.createQuery(query);
//        System.out.println(typedQuery.getResultList().size());
//        List<orderItem> result = typedQuery.getResultList();
        return entityManager.createQuery(query).getResultList();
    }
}
