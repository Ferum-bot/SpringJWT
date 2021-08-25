package com.ferum_bot.springjwt.repositories;

import com.ferum_bot.springjwt.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Collection<User> findUsersByFullName(String fullName);

    User findUserByNickname(String nickname);
}
