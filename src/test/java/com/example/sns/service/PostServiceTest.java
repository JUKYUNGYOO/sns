package com.example.sns.service;

import com.example.sns.SnsApplication;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.fixture.PostEntityFixture;
import com.example.sns.fixture.UserEntityFixture;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.PostEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {


    @Autowired
    private PostService postService;
    @MockBean
    private PostEntityRepository postEntityRepository;

    @MockBean
    private UserEntityRepository userEntityRepository;
    @Test
    void 포스트작성이_성공한경우(){
        String title = "title";
        String body = "body";
        String userName = "username";
        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        Assertions.assertDoesNotThrow(()->postService.create(title, body, userName));
    }
    @Test
    void 포스트작성시_요청한유저가_존재하지않는경우(){
        String title = "title";
        String body = "body";
        String userName = "username";
        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,()
        -> postService.create(title, body, userName));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());


    }
    @Test
    void 포스트수정이_성공한경우(){
        String title = "title";
        String body = "body";
        String userName = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);
        UserEntity userEntity = postEntity.getUser();

    //    PostEntity mockPostEntity = mock(PostEntity.class);
        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
     //   when(mockPostEntity.getUser().getId())
        Assertions.assertDoesNotThrow(()->postService.modify(title,body,userName,postId));



    }
    @Test
    void 포스트수정시_포스트가존재하지않는경우(){
        String title = "title";
        String body = "body";
        String userName = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);
        UserEntity userEntity = postEntity.getUser();

        //    PostEntity mockPostEntity = mock(PostEntity.class);
        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());
        //   when(mockPostEntity.getUser().getId())
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()->
                postService.modify(title,body,userName,postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());


    }
    @Test
    void 포스트수정시_권한이없는경우(){
        String title = "title";
        String body = "body";
        String userName = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);
        UserEntity writer = UserEntityFixture.get("userName1","password",2);

        //    PostEntity mockPostEntity = mock(PostEntity.class);
        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        //   when(mockPostEntity.getUser().getId())
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()->
                postService.modify(title,body,userName,postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());


    }
    @Test
    void 포스트삭제가_성공한경우(){
        String title = "title";
        String body = "body";
        String userName = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);
        UserEntity userEntity = postEntity.getUser();

        //    PostEntity mockPostEntity = mock(PostEntity.class);
        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        //   when(mockPostEntity.getUser().getId())
         Assertions.assertDoesNotThrow(()->postService.delete(userName,1 ));



    }
    @Test
    void 포스트삭제시_포스트가존재하지않는경우(){
        String userName = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);
        UserEntity userEntity = postEntity.getUser();

        //    PostEntity mockPostEntity = mock(PostEntity.class);
        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());
        //   when(mockPostEntity.getUser().getId())
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,()->
                postService.delete(userName,1));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND,e.getErrorCode());



    }
    @Test
    void 포스트삭제시_권한이없는경우(){
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        //    PostEntity mockPostEntity = mock(PostEntity.class);
        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);
        UserEntity writer = UserEntityFixture.get("userName1","password",2);
        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        //   when(mockPostEntity.getUser().getId())
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,()->
                postService.delete(userName,1));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND,e.getErrorCode());



    }
    @Test
    void 피드목록요청이_성공한경우(){
        Pageable pageable = mock(Pageable.class);
        //mocking
        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());
        //   when(mockPostEntity.getUser().getId())
        Assertions.assertDoesNotThrow(()->postService.list(pageable));

    }
    @Test
    void 내피드목록요청이_성공한경우(){
        Pageable pageable = mock(Pageable.class);
        UserEntity user = mock(UserEntity.class);
        //mocking
        when(userEntityRepository.findByUserName(any())).thenReturn(Optional.of(user));

        when(postEntityRepository.findAllByUser(user,pageable)).thenReturn(Page.empty());
        //   when(mockPostEntity.getUser().getId())
        Assertions.assertDoesNotThrow(()->postService.my("",pageable));

    }


}
