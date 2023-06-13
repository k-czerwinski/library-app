package com.example.library.repository;

import com.example.library.model.Book;
import com.example.library.model.enums.BookGenre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.*;

//only custom queries are tested
@DataJpaTest
@Tag("IntegrationTest")
@DisplayName("BookRepositoryIntegrationTest")
class BookRepositoryTest {
    @Autowired
    BookRepository underTest;

    @Test
    @DisplayName("DecrementBookAmount should succeed if valid bookID passed")
    void decrementBookAmountForBookWithIdShouldSucceed() {
        Long bookId = 1L;
        Book book = new Book(bookId, BookGenre.FANTASY, "The Hobbit", 1937, "J.R.R. Tolkien",20,30 );
        underTest.save(book);
        //it should decrement current book amount properly
        underTest.decrementBookAmountForBookWithId(bookId);

        assertTrue(underTest.findById(bookId).isPresent());
        assertEquals(book.getCurrentBookAmount()-1, underTest.findById(bookId).get().getCurrentBookAmount());
    }

    @Test
    @DisplayName("IncrementBookAmount should succeed if valid bookID passed")
    void incrementBookAmountForBookWithId() {
        Long bookId = 1L;
        Book book = new Book(bookId, BookGenre.FANTASY, "The Hobbit", 1937, "J.R.R. Tolkien",20,30 );
        underTest.save(book);
        //it should increment current book amount properly
        underTest.incrementBookAmountForBookWithId(bookId);

        assertTrue(underTest.findById(bookId).isPresent());
        assertEquals(book.getCurrentBookAmount()+1, underTest.findById(bookId).get().getCurrentBookAmount());
    }

    @Test
    @DisplayName("Update book amount should succeed if passed valid bookId")
    void updateBookAmount() {
        Long bookId = 1L;
        Book book = new Book(bookId, BookGenre.FANTASY, "The Hobbit", 1937, "J.R.R. Tolkien",20,30 );
        underTest.save(book);
        Integer newCurrentBookAmount = 5;
        Integer newTotalBookAmount = 10;

        //it should decrement current book amount properly
        underTest.updateBookAmount(bookId,newTotalBookAmount,newCurrentBookAmount);

        assertTrue(underTest.findById(bookId).isPresent());
        assertEquals(newCurrentBookAmount, underTest.findById(bookId).get().getCurrentBookAmount(),"Current book amount comparison:");
        assertEquals(newTotalBookAmount, underTest.findById(bookId).get().getTotalBookAmount(),"Total book amount comparison:");
    }
}