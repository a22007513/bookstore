package rocky.com.example.virtualbookstore.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import rocky.com.example.virtualbookstore.request.bookSearchRequest;
import rocky.com.example.virtualbookstore.module.bookstore_book;
import rocky.com.example.virtualbookstore.predicate.definePredicate;

import java.util.ArrayList;
import java.util.List;

@Repository
public class bookCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public  bookCriteriaRepository(EntityManager entityManager){
        this.entityManager =entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<bookstore_book> findAll(bookSearchRequest booksearchrequest){
        //initial criteria query(it give us ooc over query ) and define return class
        CriteriaQuery<bookstore_book> criteraQueryBook = criteriaBuilder.createQuery(bookstore_book.class);

        //define the select command from which entity
        Root<bookstore_book> root = criteraQueryBook.from(bookstore_book.class);
        definePredicate definePredicate = new definePredicate();
        Predicate predicate = definePredicate.bookPredicate(booksearchrequest,root,criteriaBuilder);

        //define booklist order by what column and sortMethod
        setBookListOrder(booksearchrequest,root,criteraQueryBook);

        //combination where condition with query command from predicate
        criteraQueryBook.where(predicate);

        //at here hit the database execute query
        TypedQuery<bookstore_book> typedQuery = entityManager.createQuery(criteraQueryBook);
        typedQuery.setFirstResult(booksearchrequest.getPageNumber()*booksearchrequest.getPageSize()).setMaxResults(booksearchrequest.getPageSize());
        List<bookstore_book> bookList = new ArrayList<>(typedQuery.getResultList());

        //get the number of books for page
        Long numberOfBook = countBook(booksearchrequest);

        //setting parameter for json object pageable
        Pageable pageable = setPage(booksearchrequest);

        return new PageImpl<>(bookList,pageable,numberOfBook);
    }

    public Long countBook(bookSearchRequest booksearchrequest){
        CriteriaQuery<Long> criteraQueryCount = criteriaBuilder.createQuery(Long.class);
        Root<bookstore_book> root = criteraQueryCount.from(bookstore_book.class);
        definePredicate definepredicate = new definePredicate();
        Predicate countPredicate = definepredicate.bookPredicate(booksearchrequest,root,criteriaBuilder);
        //
        criteraQueryCount.select(criteriaBuilder.count(root)).where(countPredicate);
        TypedQuery<Long> queryResult = entityManager.createQuery(criteraQueryCount);
        Long numberOfBook = queryResult.getSingleResult();
        return numberOfBook;
    }

    private void setBookListOrder(bookSearchRequest booksearchrequest,Root<bookstore_book> root,CriteriaQuery<bookstore_book> criteraQuery)
    {
        if(booksearchrequest.getSortMethod().equals(Sort.Direction.ASC))
        {
            criteraQuery.orderBy(criteriaBuilder.asc(root.get(booksearchrequest.getOrderBy())));
        }
        else{
            criteraQuery.orderBy(criteriaBuilder.desc(root.get(booksearchrequest.getOrderBy())));
        }
    }

    private Pageable setPage(bookSearchRequest booksearchrequest){
        Sort sort = Sort.by(booksearchrequest.getSortMethod(), booksearchrequest.getOrderBy());
        return PageRequest.of(booksearchrequest.getPageNumber(), booksearchrequest.getPageSize(),sort);
    }
}
