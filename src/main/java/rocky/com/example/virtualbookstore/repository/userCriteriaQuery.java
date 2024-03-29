package rocky.com.example.virtualbookstore.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import rocky.com.example.virtualbookstore.module.User;
import rocky.com.example.virtualbookstore.predicate.userPredicate;
import rocky.com.example.virtualbookstore.request.userSearchRequest;

import java.util.ArrayList;
import java.util.List;

@Repository
public class userCriteriaQuery {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public userCriteriaQuery(EntityManager entityManager){
        this.entityManager=entityManager;
        this.criteriaBuilder=entityManager.getCriteriaBuilder();
    }

    public Page<User> findAll(userSearchRequest userSearchRequest){
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        userPredicate userPredicate = new userPredicate();
        Predicate predicate = userPredicate.predicate(userSearchRequest,root,criteriaBuilder);
        criteriaQuery.where(predicate);
        //define sort method Method ASE or DESC and order by which column
        setOrder(userSearchRequest,root,criteriaQuery);
        //execute query with condition
        TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(userSearchRequest.getOffset() * userSearchRequest.getLimit());
        typedQuery.setMaxResults(userSearchRequest.getLimit());
        List<User> userList = new ArrayList<>(typedQuery.getResultList());

        Long numberOfUser = count(userSearchRequest);

        Pageable pageable = setPage(userSearchRequest);
        return new PageImpl<>(userList,pageable,numberOfUser);
    }

    public Long count (userSearchRequest userSearchRequest){

        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> root = criteriaQuery.from(User.class);
        userPredicate userPredicate = new userPredicate();
        Predicate predicate = userPredicate.predicate(userSearchRequest,root,criteriaBuilder);
        criteriaQuery.select(criteriaBuilder.count(root)).where(predicate);
        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        Long result = typedQuery.getSingleResult();
        return  result;
    }

    public void setOrder(userSearchRequest userSearchRequest,Root<?> root,CriteriaQuery<?> criteriaQuery){
        if(userSearchRequest.getSortMethod().equals(Sort.Direction.DESC)){
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(userSearchRequest.getOrderBy())));
        }
        else{
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(userSearchRequest.getOrderBy())));
        }
    }

    public Pageable setPage(userSearchRequest userSearchRequest){
        Sort sort = Sort.by(userSearchRequest.getSortMethod(),userSearchRequest.getOrderBy());
        return PageRequest.of(userSearchRequest.getOffset(), userSearchRequest.getLimit(),sort);
    }

}
