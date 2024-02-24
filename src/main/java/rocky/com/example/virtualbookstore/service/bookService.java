package rocky.com.example.virtualbookstore.service;

import org.springframework.data.domain.Page;
import rocky.com.example.virtualbookstore.dto.bookSearchRequest;
import rocky.com.example.virtualbookstore.module.bookstore_book;

public interface bookService {

    Page<bookstore_book> findallbooks(bookSearchRequest bookSearchRequest);
}
