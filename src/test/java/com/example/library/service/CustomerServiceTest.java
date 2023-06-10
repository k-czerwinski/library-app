package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.model.enums.BookGenre;
import com.example.library.model.enums.UserRole;
import com.example.library.repository.BookRepository;
import com.example.library.repository.OrderRepository;
import com.example.library.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.notNull;

class CustomerServiceTest {

    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private CustomerService underTests;

    @BeforeEach
    void setUp(){
        underTests = new CustomerService(orderRepository,bookRepository,userRepository);
    }

    @Test
    @DisplayName("Should add book to cart when book exist and its current amount is greater than 0")
    void shouldAddToCartWhenBookExist() {
        Long bookId = 50L;
        Book book = new Book(bookId, BookGenre.FANTASY, "The Hobbit", 1900, "J.R.R. Tolkien",20,30 );
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertDoesNotThrow(() -> underTests.addToCart(bookId));
        assertEquals(underTests.getCart().get(0),book);
    }

    @Test
    @DisplayName("Should throw NoSuchObjectException when book doesn't exist")
    void shouldThrowExceptionWhenAddToCartWhenBookNotExists(){
        Long bookId = 50L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(false);

        Exception exception = assertThrows(NoSuchObjectException.class, () -> underTests.addToCart(bookId));
        assertEquals("Book with given ID not exists",exception.getMessage());
    }

    @Test
    @DisplayName("Should throw NoSuchObjectException when current amount of book is lower than 1")
    void shouldThrowExceptionWhenAddToCartWhenCurrentAmountLowerThanOne(){
        Long bookId = 50L;
        Book book = new Book(bookId, BookGenre.FANTASY, "The Hobbit", 1900, "J.R.R. Tolkien",0,30 );
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(NoSuchObjectException.class,() -> underTests.addToCart(bookId));
        assertEquals("Book with given ID is not available", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when that book already added to cart")
    void shouldThrowExceptionWhenAddToCartWhenBookAlreadyInCart(){
        Long bookId = 50L;
        Book book = new Book(bookId, BookGenre.FANTASY, "The Hobbit", 1900, "J.R.R. Tolkien",20,30 );
        ReflectionTestUtils.setField(underTests, "cart", List.of(book));
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(IllegalArgumentException.class,() -> underTests.addToCart(bookId));
        assertEquals("You cannot borrow several of the same book", exception.getMessage());
    }

    @Test
    @DisplayName("Should succeed when book successfully removed")
    void removeFromCartShouldSucceed() {
        Long bookId = 50L;
        Book book = new Book(bookId, BookGenre.FANTASY, "The Hobbit", 1900, "J.R.R. Tolkien",20,30 );
        ReflectionTestUtils.setField(underTests, "cart", new ArrayList<Book>(List.of(book)));

        assertDoesNotThrow(() -> underTests.removeFromCart(bookId));
        assertTrue(underTests.getCart().isEmpty());
    }

    @Test
    @DisplayName("Should throw NoSuchObjectException when book with given id not exist in cart")
    void removeFromCartShouldThrowExceptionWhenBookNotExist() {
        Long bookId = 50L;

        Exception exception = assertThrows(NoSuchObjectException.class, () -> underTests.removeFromCart(bookId));
        assertEquals("There is no book with given ID in cart", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not exist")
    void confrimOrderShouldThrowExceptionWhenUserNotExist() {
        String email = "johnsmith@gmail.com";

        Exception exception = assertThrows(UsernameNotFoundException.class,() -> underTests.confirmOrder(email));
        assertEquals(email,exception.getMessage());
    }

    @Test
    @DisplayName("Should return all books which user already borrowed")
    void confirmOrderShouldReturnNotEmptyList(){
        String email = "johnsmith@gmail.com";
        List<Book> cartBooks = List.of(new Book(1L, BookGenre.FANTASY, "The Hobbit", 1937, "J.R.R. Tolkien",20,30 ),
                new Book(2L, BookGenre.FANTASY, "Lord of the rings", 1954, "J.R.R. Tolkien",50,50));
        List<Book> borrowedBooks = new ArrayList<>(cartBooks);
        borrowedBooks.add(new Book(3L, BookGenre.FANTASY, "The two towers", 1954, "J.R.R. Tolkien",25,25));

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);
        ReflectionTestUtils.setField(underTests,"cart", cartBooks);
        ReflectionTestUtils.setField(underTests,"booksBorrowed", borrowedBooks);

        List <Book> result = assertDoesNotThrow(() -> underTests.confirmOrder(email));
        assertEquals(cartBooks.stream().filter(b -> borrowedBooks.contains(b)).toList(), result);
    }

    @Test
    @DisplayName("Should succeed when user exists and all books can be proceeded")
    void confirmOrderShouldSuceed(){
        String email = "johnsmith@gmail.com";
        User user = new User(UserRole.CUSTOMER,"John","Smith",email,"123Password##");
        List<Book> cartBooks = List.of(new Book(1L, BookGenre.FANTASY, "The Hobbit", 1937, "J.R.R. Tolkien",20,30 ),
                new Book(2L, BookGenre.FANTASY, "Lord of the rings", 1954, "J.R.R. Tolkien",50,50));

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.doNothing().when(bookRepository).decrementBookAmountForBookWithId(notNull(Long.class));
        ReflectionTestUtils.setField(underTests,"cart",new ArrayList<>(cartBooks));

        List <Book> booksResult = assertDoesNotThrow(() -> underTests.confirmOrder(email));
        assertTrue(booksResult.isEmpty());
    }

    @Test
    @DisplayName("Remove From Borrowed Books should return false then user or book not exist")
    void removeFromBorrowedBooksShouldReturnFalse() {
        String email = "johnsmith@gmail.com";
        Long bookId = 1L;
        User user = new User(UserRole.CUSTOMER,"John","Smith",email,"123Password##");
        Book book = new Book(bookId, BookGenre.FANTASY, "The Hobbit", 1937, "J.R.R. Tolkien",20,30 );
        ReflectionTestUtils.setField(underTests,"booksBorrowed", List.of(book));
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        //should return false because user with that email not exist
        assertFalse(underTests.removeFromBorrowedBooks(bookId, "shouldfailemail@fail.com"));
        //should return false because books with given ID not exists
        assertFalse(underTests.removeFromBorrowedBooks(-1L,email));
    }
}