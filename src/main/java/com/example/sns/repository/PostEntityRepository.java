package com.example.sns.repository;

import com.example.sns.model.Post;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity,Integer> {
//repository는 구현체인 service에서 사용할 거임
    Page<PostEntity> findAllByUser(UserEntity entity, Pageable pageable);
}
