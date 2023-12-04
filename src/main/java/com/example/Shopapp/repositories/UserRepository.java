package com.example.Shopapp.repositories;

import com.example.Shopapp.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Vì kế thưa JpaRepo => java tự hiểu đó là 1 repo => có hay k k quan trọng
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT a from User a where a.phoneNumber = :phoneNumber")
//    @Query(value = "SELECT * from User as u where u.phoneNumber = :phoneNumber",nativeQuery = true )
    User findByPhone (String phoneNumber);
}
