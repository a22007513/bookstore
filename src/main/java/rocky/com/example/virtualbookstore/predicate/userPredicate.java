package rocky.com.example.virtualbookstore.predicate;

import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import rocky.com.example.virtualbookstore.request.userSearchRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class userPredicate {

    public Predicate predicate(userSearchRequest userSearchRequest, Root root, CriteriaBuilder criteriaBuilder){

        List<Predicate> predicateList = new ArrayList<>();
        if (Objects.nonNull(userSearchRequest.getSearch())) {
            predicateList.add(criteriaBuilder.like(root.get("email"), "%" + userSearchRequest.getSearch() + "%"));
        }
       return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
   }
}
