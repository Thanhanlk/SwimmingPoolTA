package com.swimmingpool.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.username = :#{@userServiceImpl.currentUserThrowIfNot.username}")
    void deleteByUsername();

    @Query("SELECT c FROM Cart c WHERE c.username = ?1 AND c.assignmentId = ?2")
    Optional<Cart> findByUsernameAndAssignmentId(String username, String assignmentId);
}
