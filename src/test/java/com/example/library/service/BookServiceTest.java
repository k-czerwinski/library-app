package com.example.library.service;

import com.example.library.exceptions.AlreadyExistException;
import com.example.library.exceptions.IncorrectParameterValueException;
import com.example.library.model.Book;
import com.example.library.model.enums.BookGenre;
import com.example.library.repository.BookRepository;
import com.sun.jdi.ObjectCollectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    private BookService underTest;

    @BeforeEach
    void setUp(){
        underTest = new BookService(bookRepository);
    }
    @Test
    @DisplayName("saveBook should throw AlreadyExistsException if book is already in library collection")
    void saveBookShouldThrowExceptionIfBookAlreadyStoredInLibraryCollection() {
        Book book = new Book(1L, BookGenre.FANTASY, "The Hobbit", 1900, "J.R.R. Tolkien",20,30 );
        Mockito.when(bookRepository.existsByTitleAndPublicationYearAndAuthor(any(String.class),any(int.class),any(String.class)))
                        .thenReturn(true);

        assertThrows(AlreadyExistException.class,() -> underTest.saveBook(book));
    }

    @Test
    @DisplayName("saveBook should succeed")
    void saveBookShouldSucceed(){
        Book book = new Book(1L, BookGenre.FANTASY, "The Hobbit", 1900, "J.R.R. Tolkien",20,30 );
        Mockito.when(bookRepository.existsByTitleAndPublicationYearAndAuthor(any(String.class),any(int.class),any(String.class)))
                .thenReturn(false);

        assertDoesNotThrow(() -> underTest.saveBook(book));
        Mockito.verify(bookRepository,Mockito.times(1)).save(book);
    }

    @Test
    @DisplayName("updateBookAmount should throw ObjectCollectedException if book wasn't found")
    void updateBookAmountShouldThrowExceptionIfBookCouldntBeFound() {
        Long bookId = 10L;
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ObjectCollectedException.class,() -> underTest.updateBookAmount(bookId,100));
    }

    @Test
    @DisplayName("updateBookAmount should throw IncorrectParameterValueException if new total book amount is smaller than currently borrowed")
    void updateBookAmountShouldThrowExceptionIfIncorrectNewBookAmount() {
        Long bookId = 1L;
        //minimum new total book amount is 10
        Book book = new Book(bookId, BookGenre.FANTASY, "The Hobbit", 1900, "J.R.R. Tolkien",20,30 );
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(IncorrectParameterValueException.class,() -> underTest.updateBookAmount(bookId,5));
    }

    @Test
    @DisplayName("updateBookAmount should succeed")
    void updateBookAmountShouldSucceed() {
        Long bookId = 1L;
        int newTotalBookAmount = 30;
        //minimum new total book amount is 10
        Book book = new Book(bookId, BookGenre.FANTASY, "The Hobbit", 1900, "J.R.R. Tolkien",20,30 );
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertDoesNotThrow(() -> underTest.updateBookAmount(bookId,newTotalBookAmount));
        Mockito.verify(bookRepository,Mockito.times(1)).updateBookAmount(bookId,newTotalBookAmount,newTotalBookAmount - (book.getTotalBookAmount() - book.getCurrentBookAmount()));
    }
}