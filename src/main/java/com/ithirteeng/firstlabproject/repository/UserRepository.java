package com.ithirteeng.firstlabproject.repository;

import com.ithirteeng.firstlabproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
