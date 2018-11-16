package com.rxproject.rosbank.repository;

import com.rxproject.rosbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.cards c where c.cardNumber = :cardNo")
    User findByCardNo(@Param("cardNo") String cardNo);

}
