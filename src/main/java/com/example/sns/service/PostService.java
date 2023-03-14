package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.Post;
import com.example.sns.model.User;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.PostEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

        private final PostEntityRepository postEntityRepository;
        private final UserEntityRepository userEntityRepository;

        @Transactional
        public void create(String title, String body, String userName){
            //user find
               UserEntity userEntity =  userEntityRepository.findByUserName(userName).orElseThrow(()->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                                String.format("%s not founded",userName)));
            //post save
           postEntityRepository.save(PostEntity.of(title,body,userEntity));
               //저장된 PostEntity를 받아옴
            //return
        }

        @Transactional
        public Post modify(String title, String body, String userName, Integer postId){
            //user find
            UserEntity userEntity = userEntityRepository.findByUserName(userName)
                    .orElseThrow(() ->
                            new SnsApplicationException(ErrorCode.USER_NOT_FOUND
                            , String.format("%s not founded", userName)));
            //post exist
            PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(()->
                    new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
                            String.format("%s not founded",postId)));
            //post permission
            if(postEntity.getUser() != userEntity){
                throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,
                        String.format("%s has no permission with %s",userName,postId));
            }
            postEntity.setTitle(title);
            postEntity.setBody(body);
            return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
        }

}
