package rocky.com.example.virtualbookstore.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rocky.com.example.virtualbookstore.request.bookSearchRequest;
import rocky.com.example.virtualbookstore.module.bookstore_book;
import rocky.com.example.virtualbookstore.repository.bookRepository;
import rocky.com.example.virtualbookstore.service.bookService;
import rocky.com.example.virtualbookstore.constant.bookCategory;


@RestController
@Validated
public class bookController {

    private static final Logger logger = LoggerFactory.getLogger(bookController.class);
    @Autowired
    private bookRepository bookRepository;


    @Autowired
    private bookService bookService;

    @GetMapping("/book/{bookid}")
    public ResponseEntity<bookstore_book> getbookByid(@PathVariable Integer bookid){
        bookstore_book book = bookRepository.findById(bookid).orElse(null);
        return  ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @GetMapping("/books")
    public ResponseEntity<Page<bookstore_book>> getbooks(
                                         //query filter
                                         @RequestParam (required = false) bookCategory category,
                                         @RequestParam (required = false) String search,
                                         //pagination
                                         @RequestParam (defaultValue = "0") @Min(0)  Integer offset,
                                         @RequestParam (defaultValue = "10")@Min(0) @Max(100) Integer limit,
                                         @RequestParam (defaultValue = "publicationDate") String orderBy,
                                         @RequestParam (defaultValue = "DESC")String sortMethod){
        bookSearchRequest bookSearchRequest = new bookSearchRequest();
        bookSearchRequest.setBookName(search);
        bookSearchRequest.setCategeory(category);
        bookSearchRequest.setPageNumber(offset);
        bookSearchRequest.setPageSize(limit);
        bookSearchRequest.setOrderBy(orderBy);
        Sort.Direction sort = Sort.Direction.fromString(sortMethod);
        bookSearchRequest.setSortMethod(sort);
        Page<bookstore_book> bookList = bookService.findallbooks(bookSearchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @PostMapping("/book/addbook")
    public ResponseEntity<bookstore_book> addbook(@RequestBody bookstore_book book){
        bookRepository.save(book);
        bookstore_book newbook =  bookRepository.findById(book.getBookid()).orElse(null);
        return  ResponseEntity.status(HttpStatus.CREATED).body(newbook);
    }

    @DeleteMapping("/book/{bookid}")
    public ResponseEntity<?> deleteBookByid(@PathVariable Integer bookid)
    {
        bookRepository.deleteById(bookid);
        logger.info("bookid {} has been deleted",bookid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/book/{bookid}")
    public ResponseEntity<bookstore_book> updateBookByid(@PathVariable Integer bookid,
            @RequestBody bookstore_book book)
    {
        book.setBookid(bookid);
        bookstore_book  updateBook= bookRepository.findById(book.getBookid()).orElse(null);
        if(updateBook!=null)
        {
            bookRepository.save(book);
            return ResponseEntity.status(HttpStatus.OK).body(updateBook);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
