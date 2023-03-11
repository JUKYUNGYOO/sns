package com.example.sns.model;


import com.example.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

//서비스 단에서 처리되는 DTO 클래스
@AllArgsConstructor
@Getter
public class User {

    private Integer id;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

//entity에 있는 field 들을 DTO 클래스로 변환

    public static User fromEntity(UserEntity entity){
        return new User(
                entity.getId(),
                entity.getUserName(),
                entity.getPassword(),
                entity.getRole(),
                entity.getRegisteredAt(),
                entity.getUpdateAt(),
                entity.getDeletedAt()


        );
    }

}
