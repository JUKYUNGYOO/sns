package com.example.sns.controller;


import com.example.sns.controller.request.PostCreateRequest;
import com.example.sns.controller.request.PostModifyRequest;
import com.example.sns.controller.request.UserLoginRequest;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.fixture.PostEntityFixture;
import com.example.sns.model.Post;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import com.example.sns.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;
    @Autowired
    private UserEntityRepository userEntityRepository;


    @Test
    @WithMockUser
    void ???????????????() throws Exception{
        String title = "title";
        String body = "body";
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo: add request body
                        .content(objectMapper.writeValueAsBytes
                                (new PostCreateRequest(title, body)))


                ).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @WithAnonymousUser
    void ??????????????????_???????????????????????????() throws Exception{
        String title = "title";
        String body = "body";
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo: add request body
                        .content(objectMapper.writeValueAsBytes
                                (new PostCreateRequest(title, body)))


                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    void ???????????????() throws Exception{
        String title = "title";
        String body = "body";
        when(postService.modify(eq(title), eq(body),any(),any())).
                thenReturn(Post.fromEntity(PostEntityFixture.get("userName",1,1)));

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo: add request body
                        .content(objectMapper.writeValueAsBytes
                                (new PostModifyRequest(title, body)))


                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void ??????????????????_???????????????????????????() throws Exception{
        String title = "title";
        String body = "body";
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo: add request body
                        .content(objectMapper.writeValueAsBytes
                                (new PostModifyRequest(title, body)))


                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    void ??????????????????_?????????_?????????_??????_????????????_????????????() throws Exception {
        String title = "title";
        String body = "body";
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title),
                eq(body),any(),eq(1));
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title,body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void ??????????????????_?????????????????????_?????????_????????????() throws Exception {
        String title = "title";
        String body = "body";
        doThrow(new SnsApplicationException
                (ErrorCode.INVALID_PERMISSION))
                .when(postService).modify(eq(title),eq(body),any(),eq(1));
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title,body))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }


//    @Test
//    void ??????????????????_???????????????()  {
//        String title = "title";
//        String body = "body";
//        String userName = "userName";
//
//
//
//
//        // mocking
//        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
//        when(userEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
//
//        Assertions.assertDoesNotThrow(()-> postService.create(title,body,userName));
//
//    }
//    @Test
//    void ??????????????????_?????????????????????() throws Exception {
//        String title = "title";
//        String body = "body";
//        String userName = "userName";
//        Integer postId = 1;
//
//        // mocking
//        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
//        when(userEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
//        Assertions.assertDoesNotThrow(()-> postService.modify(title,body,userName,postId));
//
//    }
    @Test
    @WithMockUser
    void ???????????????() throws Exception{
        mockMvc.perform(delete("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());


    }
    @Test
    @WithAnonymousUser
    void ??????????????????_???????????????????????????() throws Exception{
        mockMvc.perform(delete("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    void ??????????????????_??????????????????????????????????????????() throws Exception{
        //mocking
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(postService).delete(any(),any());


        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    void ??????????????????_???????????????_????????????_????????????????????????() throws Exception{
        //mocking
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND))
                .when(postService).delete(any(),any());

        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void ???????????????() throws Exception{
        when(postService.my(any(),any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/my")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());


    }
    @Test
    @WithAnonymousUser
    void ????????????????????????_???????????????????????????() throws Exception{
        when(postService.my(any(),any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/my")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }





}
