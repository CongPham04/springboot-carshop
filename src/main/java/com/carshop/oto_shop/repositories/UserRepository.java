package com.carshop.oto_shop.repositories;

import com.carshop.oto_shop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsById(String userId);
    @Modifying
    @Query("DELETE FROM User u WHERE u.account.accountId = :accountId")
    void deleteAllByAccountId(@Param("accountId") String accountId);

    // Find user by account username (1:1 relationship)
    Optional<User> findByAccount_Username(String username);
}
