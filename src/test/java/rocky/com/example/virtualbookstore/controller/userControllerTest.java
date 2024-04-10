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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import rocky.com.example.virtualbookstore.request.authenticationRequest;
import rocky.com.example.virtualbookstore.request.registerRequest;
import rocky.com.example.virtualbookstore.request.userRequest;
import rocky.com.example.virtualbookstore.request.userUpdateRequest;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
//@ActiveProfiles("test")
public class userControllerTest {

    @Autowired
    MockMvc mockMvc;


    @Test
    public void authenticationsuccess() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        authenticationRequest authenticationRequest = new authenticationRequest();
        authenticationRequest.setEmail("admin@bookstore.com");
        authenticationRequest.setPassword("admin");
        String json = objectMapper.writeValueAsString(authenticationRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.token",notNullValue()));
    }

    @Test
    public void authenticationwrongparameter() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void authenticationwrongpassword() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        authenticationRequest authenticationRequest = new authenticationRequest();
        authenticationRequest.setEmail("admin@bookstore.com");
        authenticationRequest.setPassword("admin1");
        String json = objectMapper.writeValueAsString(authenticationRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    @Transactional
    public void registersuccess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        registerRequest registerRequest = new registerRequest();
        registerRequest.setEmail("test4@bookstore.com");
        registerRequest.setPassword("test4");
        registerRequest.setRole("ROLE_USER");
        String json = objectMapper.writeValueAsString(registerRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.email",equalTo("test4@bookstore.com")))
                .andExpect(jsonPath("$.createDate",notNullValue()))
                .andExpect(jsonPath("$.lastModifyDate",notNullValue()))
                .andExpect(jsonPath("role",equalTo("ROLE_USER")));
    }

    @Test
    public void queryUserSuccess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        authenticationRequest authenticationRequest = new authenticationRequest();
        authenticationRequest.setEmail("admin@bookstore.com");
        authenticationRequest.setPassword("admin");
        String json = objectMapper.writeValueAsString(authenticationRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"token\":\"","");
        String token = response.replace("\"}","");
        RequestBuilder requestBuilder1 = MockMvcRequestBuilders.get("/api/auth/user/finduser")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization" ,"Bearer "+token)
                .param("search","test")
                .param("limit","10")
                .param("offset","0")
                .param("orderBy","createDate")
                .param("sortMethod","ASC");
        mockMvc.perform(requestBuilder1)
                .andDo(print())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.pageable.pageNumber",equalTo(0)))
                .andExpect(jsonPath("$.pageable.pageSize",equalTo(10)))
                .andExpect(jsonPath("$.content[0].email",equalTo("test1@bookstore.com")))
                .andExpect(jsonPath("$.content[0].userid",notNullValue()))
                .andExpect(jsonPath("$.content[0].createDate",notNullValue()))
                .andExpect(jsonPath("$.content[0].lastModifyDate",notNullValue()))
                .andExpect(jsonPath("$.content[0].role",equalTo("ROLE_USER")));
    }


    @Test
    public void queryUserWrongParameter() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        authenticationRequest authenticationRequest = new authenticationRequest();
        authenticationRequest.setEmail("test1@bookstore.com");
        authenticationRequest.setPassword("test1");
        String json = objectMapper.writeValueAsString(authenticationRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"token\":\"","");
        String token = response.replace("\"}","");
        RequestBuilder requestBuilder1 = MockMvcRequestBuilders.get("/api/auth/user/finduser")
                .contentType(MediaType.APPLICATION_JSON)
                .param("search","test")
                .param("limit","10")
                .param("offset","0")
                .param("orderBy","createDate")
                .param("sortMethod","ASC");
        mockMvc.perform(requestBuilder1)
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void updateUserPasswordSuccess() throws Exception {
        //get jwt token
        ObjectMapper objectMapper = new ObjectMapper();
        authenticationRequest authenticationRequest = new authenticationRequest();
        authenticationRequest.setEmail("admin@bookstore.com");
        authenticationRequest.setPassword("admin");
        String json = objectMapper.writeValueAsString(authenticationRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        MvcResult mvcResult =  mockMvc.perform(requestBuilder).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        response = response.replace("{\"token\":\"","");
        String token = response.replace("\"}", "");
        //test start
        userUpdateRequest userUpdateRequest = new userUpdateRequest();
        userUpdateRequest.setEmail("test3@bookstore.com");
        userUpdateRequest.setPassword("test3");
        userUpdateRequest.setUpdatePassword("test3password");
        String body = objectMapper.writeValueAsString(userUpdateRequest);
        RequestBuilder updatePassword = MockMvcRequestBuilders.put("/api/auth/user/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+token)
                .content(body);
        mockMvc.perform(updatePassword)
                .andDo(print())
                .andExpect(status().is(201));
    }

    @Test
    public void updateUserPasswordWrongParameter() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        //test start
        userUpdateRequest userUpdateRequest = new userUpdateRequest();
        userUpdateRequest.setEmail("admin@bookstore.com");
        userUpdateRequest.setPassword("test3password");
        userUpdateRequest.setUpdatePassword("admin");
        String body = objectMapper.writeValueAsString(userUpdateRequest);
        RequestBuilder updatePassword = MockMvcRequestBuilders.put("/api/auth/user/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        mockMvc.perform(updatePassword)
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void updateUserAuthoritySuccess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        authenticationRequest authenticationRequest = new authenticationRequest();
        authenticationRequest.setEmail("admin@bookstore.com");
        authenticationRequest.setPassword("admin");
        String json = objectMapper.writeValueAsString(authenticationRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        MvcResult mvcResult =  mockMvc.perform(requestBuilder).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        response = response.replace("{\"token\":\"","");
        String token = response.replace("\"}", "");
        userRequest userRequest = new userRequest();
        userRequest.setEmail("test1@bookstore.com");
        String body = objectMapper.writeValueAsString(userRequest);
        RequestBuilder updatePassword = MockMvcRequestBuilders.put("/api/auth/user/admin/modifyauthority")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+token)
                .content(body);
        mockMvc.perform(updatePassword)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.email",equalTo("test1@bookstore.com")))
                .andExpect(jsonPath("$.createDate",notNullValue()))
                .andExpect(jsonPath("$.lastModifyDate",notNullValue()))
                .andExpect(jsonPath("$.role",equalTo("ROLE_ADMIN")));
    }

    @Test
    public void updateUserAuthorityWorngAuthority() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        authenticationRequest authenticationRequest = new authenticationRequest();
        authenticationRequest.setEmail("test3@bookstore.com");
        authenticationRequest.setPassword("test3password");
        String json = objectMapper.writeValueAsString(authenticationRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        MvcResult mvcResult =  mockMvc.perform(requestBuilder).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        response = response.replace("{\"token\":\"","");
        String token = response.replace("\"}", "");
        userRequest userRequest = new userRequest();
        userRequest.setEmail("test1@bookstore.com");
        String body = objectMapper.writeValueAsString(userRequest);
        RequestBuilder updatePassword = MockMvcRequestBuilders.put("/api/auth/user/admin/modifyauthority")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+token)
                .content(body);
        mockMvc.perform(updatePassword)
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void updateUserAuthorityWrongParameter() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        userRequest userRequest = new userRequest();
        userRequest.setEmail("test1@bookstore.com");
        String body = objectMapper.writeValueAsString(userRequest);
        RequestBuilder updatePassword = MockMvcRequestBuilders.put("/api/auth/user/admin/modifyauthority")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        mockMvc.perform(updatePassword)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.email",equalTo("test1@bookstore.com")))
                .andExpect(jsonPath("$.createDate",notNullValue()))
                .andExpect(jsonPath("$.lastModifyDate",notNullValue()))
                .andExpect(jsonPath("$.role",equalTo("ROLE_ADMIN")));
    }
}
