package com.ithirteeng.firstlabproject.repository;

import com.ithirteeng.firstlabproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByLogin(String login);

    Boolean existsByLogin(String login);
}
