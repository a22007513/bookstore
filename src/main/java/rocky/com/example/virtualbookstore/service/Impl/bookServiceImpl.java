package rocky.com.example.virtualbookstore.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import rocky.com.example.virtualbookstore.dto.bookSearchRequest;
import rocky.com.example.virtualbookstore.module.bookstore_book;
import rocky.com.example.virtualbookstore.repository.bookCriteriaRepository;
import rocky.com.example.virtualbookstore.service.bookService;

@Component
public class bookServiceImpl implements bookService {
    private final bookCriteriaRepository bookCriteriaRepository;

    public bookServiceImpl(bookCriteriaRepository bookCriteriaRepository) {
        this.bookCriteriaRepository = bookCriteriaRepository;
    }


    @Override
    public Page<bookstore_book> findallbooks(bookSearchRequest bookSearchRequest) {

        return bookCriteriaRepository.findAll(bookSearchRequest);
    }
}
