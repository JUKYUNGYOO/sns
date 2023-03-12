package com.example.sns.controller;


import com.example.sns.controller.request.UserJoinRequest;
import com.example.sns.controller.request.UserLoginRequest;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.User;
import com.example.sns.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // api test
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; //api test를 위해 MockMvc 정의

    @Autowired
    private  ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void 회원가입() throws Exception {
        String userName = "userName";
        String password = "password";

        //to do : mocking - 회원가입이 정상적으로 될 때, 반환 값
        when(userService.join(userName, password)).thenReturn (mock(User.class));

        //회원가입은 post 메소드로 전송되므로
        mockMvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                // todo: add request body
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest("userName","password")))


        ).andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void 회원가입시_이미_회원가입된_username으로_회원가입하는_경우_에러반환() throws Exception{
        String userName = "userName";
        String password = "password";
        
        //todo : mocking
        when(userService.join(userName,password)).thenThrow(
                new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        //회원가입은 post 메소드로 전송되므로
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo: add request body
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest("userName","password")))

                ).andDo(print())
                .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getStatus().value()));

    }
    @Test
    public void 로그인() throws Exception {
        String userName = "userName";
        String password = "password";

        //to do : mocking - 회원가입이 정상적으로 될 때, 반환 값
        when(userService.login(userName, password)).thenReturn("testToken");

        //회원가입은 post 메소드로 전송되므로
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo: add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("userName","password")))


                ).andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void 로그인시_회원가입이_안된_username을_입력시_에러반환() throws Exception {
        String userName = "userName";
        String password = "password";

        //to do : mocking - 회원가입이 정상적으로 될 때, 반환 값
        when(userService.login(userName, password)).
                thenThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

        //회원가입은 post 메소드로 전송되므로
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo: add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("userName","password")))


                ).andDo(print())
                .andExpect(status().is(ErrorCode.USER_NOT_FOUND.getStatus().value()));
    }
    @Test
    public void 로그인시_틀린_password를_입력할경우_에러반환() throws Exception {
        String userName = "userName";
        String password = "password";

        //to do : mocking - 회원가입이 정상적으로 될 때, 반환 값
        when(userService.join(userName,password)).
                thenThrow(new SnsApplicationException(ErrorCode.INVALID_PASSWORD));


        //회원가입은 post 메소드로 전송되므로
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo: add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("userName","password")))


                ).andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getStatus().value()));

    }

}
