package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
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
               PostEntity saved =  postEntityRepository.save(PostEntity.of(title,body,userEntity));
               //저장된 PostEntity를 받아옴
            //return
        }

}
