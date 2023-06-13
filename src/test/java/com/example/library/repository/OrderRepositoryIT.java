package com.example.library.repository;


import com.example.library.model.Book;
import com.example.library.model.Order;
import com.example.library.model.User;
import com.example.library.model.enums.BookGenre;
import com.example.library.model.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Tag("IntegrationTest")
@DisplayName("OrderRepositoryIntegrationTest")
class OrderRepositoryTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    OrderRepository underTest;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Should update borrowed book status")
    void updateBorrowedBookStatusTest() {
        String email = "johnsmith@gmail.com";
        Book book = new Book(1L, BookGenre.FANTASY, "The Hobbit", 1937, "J.R.R. Tolkien",20,30 );
        User user = new User(UserRole.CUSTOMER,"John","Smith",email,"123Password##");
        List <Book> booksBorrowed = List.of(book);
        Order order = new Order(user, booksBorrowed);

        userRepository.save(user);
        bookRepository.save(book);
        //orderId is generated automatically
        underTest.save(order);

        Order savedOrder = underTest.findAll().stream().findAny().get();
        Long orderId = savedOrder.getOrderId();
        Book savedBook = savedOrder.getBooksBorrowed().entrySet().stream().filter(e -> !e.getValue()).findFirst().get().getKey();
        Long bookId = savedBook.getBookId();

        underTest.updateBorrowedBookStatus(orderId, bookId);

        assertTrue(
                underTest.findAll().stream().filter(o -> o.getOrderId().equals(orderId)).findFirst().get()
                        .getBooksBorrowed().entrySet().stream().filter(e -> e.getKey().getBookId().equals(bookId)).findFirst().get().getValue(),
                "Book returned status comparison:");
    }
}