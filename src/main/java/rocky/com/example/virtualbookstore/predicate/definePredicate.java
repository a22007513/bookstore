package rocky.com.example.virtualbookstore.predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import rocky.com.example.virtualbookstore.request.bookSearchRequest;
import rocky.com.example.virtualbookstore.module.bookstore_book;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class definePredicate {

    public Predicate bookPredicate(bookSearchRequest bookSearchCriteria, Root<bookstore_book> root, CriteriaBuilder ceriterBuilder){

        List<Predicate> predicateList = new ArrayList<>();
        if(Objects.nonNull(bookSearchCriteria.getBookName())){
            predicateList.add(
                    ceriterBuilder.like(root.get("bookName"),  "%" + bookSearchCriteria.getBookName() + "%" )
            );
        }
        if(Objects.nonNull(bookSearchCriteria.getCategeory())){
            predicateList.add(
                    ceriterBuilder.equal(root.get("category"),   bookSearchCriteria.getCategeory() )
            );
        }
        return ceriterBuilder.and(predicateList.toArray(new Predicate[0]));
    }
}
