package com.example.sns.model.entity;

import com.example.sns.model.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "\"post\"")
@Getter
@Setter
@SQLDelete(sql = "UPDATED \"post\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;
    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
//post를 가지고 올때, user_id로 조인해서 가지고 옴

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "updated_at")
    private Timestamp updateAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    public static PostEntity of(String title,String body,UserEntity userEntity){
        PostEntity entity = new PostEntity();
        entity.setTitle(title);
        entity.setBody(body);
        entity.setUser(userEntity);
        return entity;
    }
}