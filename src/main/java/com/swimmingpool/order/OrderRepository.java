package com.swimmingpool.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o WHERE o.assignmentId IN ?1")
    List<Order> findByAssignmentId(List<String> ids);

    @Query("SELECT o FROM Order o WHERE o.assignmentId = ?1 AND o.createdBy = :#{@userServiceImpl.currentUserThrowIfNot.username}")
    Optional<Order> findByAssignmentIdAndCurrentUser(String assignmentId);
}
