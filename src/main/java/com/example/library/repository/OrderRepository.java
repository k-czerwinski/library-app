package com.example.library.repository;

import com.example.library.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long>{
    public List<Order> findAllByUserId(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE books_borrowed SET was_returned = 1 where books_borrowed_key = :bookId AND order_id = :orderId", nativeQuery = true)
    public void updateBorrowedBookStatus(@Param("bookId") Long bookId, @Param("orderId") Long orderId);
}
