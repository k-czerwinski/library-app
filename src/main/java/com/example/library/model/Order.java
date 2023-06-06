package com.example.library.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate orderDate;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User user;

    @ElementCollection
    @CollectionTable(name = "books_borrowed",
        joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "order_id")})
    @MapKeyColumn(name = "book_id")
    @Column(name = "was_returned")
    private Map<Book, Boolean> booksBorrowed;

    public Order() {
    }

    public Order(User user, List<Book> booksBorrowed){
        this.user = user;
        this.booksBorrowed = booksBorrowed.stream().collect(Collectors.toMap(b->b, b->false));
    }

    public Map<Book,Boolean> getBooksBorrowed() {
        return booksBorrowed;
    }

    public void setBooksBorrowed(Map<Book,Boolean> booksBorrowed) {
        this.booksBorrowed = booksBorrowed;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}