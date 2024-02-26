package rocky.com.example.virtualbookstore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import rocky.com.example.virtualbookstore.constant.bookCategory;
import rocky.com.example.virtualbookstore.module.bookstore_book;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class bookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void findbookByid() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/{bookid}",1)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.bookid",equalTo(1)))
                .andExpect(jsonPath("$.author",equalTo("J.K.Rowling")))
                .andExpect(jsonPath("$.bookName",equalTo("Harry Potter and Sorcerer's Stone")))
                .andExpect(jsonPath("$.category",equalTo("Fantasy")))
                .andExpect(jsonPath("$.price",notNullValue()))
                .andExpect(jsonPath("$.description",notNullValue()))
                .andExpect(jsonPath("$.stock",notNullValue()))
                .andExpect(jsonPath("$.language",notNullValue()))
                .andExpect(jsonPath("$.publicationDate",notNullValue()));
    }

    @Test
    public void findbookByid_noneid() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/{bookid}",100)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void findbooks_success() throws Exception {
        //requestparameter name in the url must same as we define in the controller
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books")
                .param("search","percy jackson")
                .param("orderBy","publicationDate")
                .param("offset","0")
                .param("limit","5")
                .param("sortMethod","ASC")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].bookName",equalTo("percy jackson & the olympians the lightning thief")))
                .andExpect(jsonPath("$.content[0].author",equalTo("Rick Riordan")))
                .andExpect(jsonPath("$.content[0].category",equalTo("Fantasy")))
                .andExpect(jsonPath("$.content[0].price",notNullValue()))
                .andExpect(jsonPath("$.content[0].description",notNullValue()))
                .andExpect(jsonPath("$.content[0].stock",notNullValue()))
                .andExpect(jsonPath("$.content[0].language",notNullValue()))
                .andExpect(jsonPath("$.content[0].publicationDate",notNullValue()))
                .andExpect(jsonPath("$.pageable.pageNumber",equalTo(0)))
                .andExpect(jsonPath("$.pageable.pageSize",equalTo(5)));
    }

    @Test
    public void updateBook() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        bookstore_book book = new bookstore_book();
        book.setBookName("Courage to be Disliked");
        book.setAuthor("Ichiro Kishimi & Fumitake Kog");
        bookCategory category = bookCategory.Self_help;
        book.setCategory(category);
        book.setDescription("help you to find the point of life");
        book.setLanguage("Japanese");
        book.setPrice(237);
        book.setStock(3);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2013-12-12 08:00:00");
        book.setPublicationDate(date);
        String json = objectMapper.writeValueAsString(book);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/{bookid}",4)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.bookName",equalTo("Courage to be Disliked")))
                .andExpect(jsonPath("$.author",equalTo("Ichiro Kishimi & Fumitake Kog")))
                .andExpect(jsonPath("$.category",equalTo("Self_help")))
                .andExpect(jsonPath("$.price",equalTo(237)))
                .andExpect(jsonPath("$.description",notNullValue()))
                .andExpect(jsonPath("$.stock",notNullValue()))
                .andExpect(jsonPath("$.language",notNullValue()))
                .andExpect(jsonPath("$.publicationDate",notNullValue()));
    }

    @Test
    public void updateBook_noncontent() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        bookstore_book book = new bookstore_book();
        book.setBookName("Courage to be Disliked");
        book.setAuthor("Ichiro Kishimi & Fumitake Kog");
        bookCategory category = bookCategory.Self_help;
        book.setCategory(category);
        book.setDescription("help you to find the point of life");
        book.setLanguage("Japanese");
        book.setPrice(237);
        book.setStock(3);
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        book.setPublicationDate(date);
        String json = objectMapper.writeValueAsString(book);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/{bookid}",20)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void updateBook_illeagument() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        bookstore_book book = new bookstore_book();
        book.setBookName("Courage to be Disliked");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2013-12-12 08:00:00");
        Timestamp timestamp = new Timestamp(date.getTime());
        book.setPublicationDate(timestamp);
        String json = objectMapper.writeValueAsString(book);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/{bookid}",20)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void delete_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/book/{bookid}",4)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(204));
    }
    @Test
    public void delete_noneid() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/book/{bookid}",420)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    @Transactional
    public void createbook_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        bookstore_book book = new bookstore_book();
        book.setBookName("No Rules Rules: Netflix and the Culture of Reinvention");
        book.setAuthor("Reed Hastings & Erin Meyer");
        bookCategory category = bookCategory.Biography;
        book.setCategory(category);
        book.setDescription("Introduction about netflix culture");
        book.setLanguage("English");
        book.setPrice(399);
        book.setStock(1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2013-12-12 08:00:00");
        book.setPublicationDate(date);
        String json = objectMapper.writeValueAsString(book);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/book/addbook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.bookName",equalTo("No Rules Rules: Netflix and the Culture of Reinvention")))
                .andExpect(jsonPath("$.author",equalTo("Reed Hastings & Erin Meyer")))
                .andExpect(jsonPath("$.category",equalTo("Biography")))
                .andExpect(jsonPath("$.price",equalTo(399)))
                .andExpect(jsonPath("$.description",notNullValue()))
                .andExpect(jsonPath("$.stock",equalTo(1)))
                .andExpect(jsonPath("$.language",notNullValue()))
                .andExpect(jsonPath("$.publicationDate",notNullValue()));
    }

    @Test
    @Transactional
    public void createbook_illeagargument() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        bookstore_book book = new bookstore_book();
        book.setBookName("No Rules Rules: Netflix and the Culture of Reinvention");
        book.setAuthor("Reed Hastings & Erin Meyer");
        bookCategory category = bookCategory.Biography;
        book.setCategory(category);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2013-12-12 08:00:00");
        book.setPublicationDate(date);
        String json = objectMapper.writeValueAsString(book);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/book/addbook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(404));
    }
}