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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        @Transactional
        public void delete(String userName, Integer postId){
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
            postEntityRepository.delete(postEntity);
        }
        public Page<Post> list(Pageable pageable){
            return postEntityRepository.findAll(pageable).map(Post::fromEntity);
        }
        //my post는 user정보가 필요하므로, String userName을 받아옴
        public Page<Post> my(String userName, Pageable pageable){
            UserEntity userEntity = userEntityRepository.findByUserName(userName)
                    .orElseThrow(() ->
                            new SnsApplicationException(ErrorCode.USER_NOT_FOUND
                                    , String.format("%s not founded", userName)));
//user가 작성한 post중에서 paging을 해야 되므로
            return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
        }

}
