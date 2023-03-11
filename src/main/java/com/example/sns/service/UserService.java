package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.User;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserEntityRepository userEntityRepository;

    //todo : implement
    public User join(String userName, String password){
        //회원가입된 userName으로 회원가입된 user가 있는지 체크 - findByUserName
        userEntityRepository.findByUserName(userName).ifPresent(it ->
        {
//이미 기존 userName이 있으면 에러던짐
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,
                    String.format("%s is duplicated", userName));
        });

        //회원가입진행 -  user 등록
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName,password));
        return User.fromEntity(userEntity);
        //User클래스로 반환 해줌
    }

    //로그인 성공 시 토큰 반환
    public String login(String userName,String password){
        //회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(()-> new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,""));
        //비밀번호 체크 - 등록된 pw와 입력받은 pw가 다를 경우 에러를 던져 줌
        if(!userEntity.getPassword().equals(password)){
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,"");
        }
        //토큰 생성
        return "";

    }
}
