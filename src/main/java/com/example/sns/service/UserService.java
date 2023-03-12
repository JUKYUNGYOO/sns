package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.User;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import com.example.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;

    //configuration에 있는 키값을 @Value로 받아옴
    @Value("${jwt.secret-key}")
    private String secretKey;


    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;


    //todo : implement
    @Transactional
    public User join(String userName, String password){
        //회원가입된 userName으로 회원가입된 user가 있는지 체크 - findByUserName
        userEntityRepository.findByUserName(userName).ifPresent(it ->
        {
//이미 기존 userName이 있으면 에러던짐
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,
                    String.format("%s is duplicated", userName));
        });

        //회원가입진행 -  user 등록
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName,encoder.encode(password)));
 //       throw new RuntimeException();
        return User.fromEntity(userEntity);
        //User클래스로 반환 해줌
     //   throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,
       //         String.format("%s is duplicated", userName));
    }

    //로그인 성공 시 토큰 반환
    public String login(String userName,String password){
        //회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(()-> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("%s not founded",userName)));
        //비밀번호 체크 - 등록된 pw와 입력받은 pw가 다를 경우 에러를 던져 줌
        if(!encoder.matches(password,userEntity.getPassword())){
                throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD, "");
        }
        //토큰 생성
        String token = JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs );
        return token;
        //토큰 생성은 util을 만들어서 정의

     }
}
