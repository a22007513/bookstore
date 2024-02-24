package rocky.com.example.virtualbookstore.repository;

import org.springframework.data.repository.CrudRepository;
import rocky.com.example.virtualbookstore.module.bookstore_book;

public interface bookRepository extends CrudRepository<bookstore_book,Integer> {
}
