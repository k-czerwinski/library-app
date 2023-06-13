package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.Order;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.OrderRepository;
import com.example.library.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CustomerService {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private List<Book> cart = new ArrayList<Book>();
    private List<Book> booksBorrowed = new ArrayList<Book>();

    @Autowired
    public CustomerService(OrderRepository orderRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public void addToCart(Long bookId) throws NoSuchObjectException,IllegalArgumentException {
        if (!bookRepository.existsById(bookId)) throw new NoSuchObjectException("Book with given ID not exists");
        Book book = bookRepository.findById(bookId).get();
        if (book.getCurrentBookAmount() < 1) throw new NoSuchObjectException("Book with given ID is not available");
        if (cart.stream().anyMatch(b -> b.getBookId().equals(bookId)))
            throw new IllegalArgumentException("You cannot borrow several of the same book");
        cart.add(book);
    }

    public void removeFromCart(Long bookId) throws NoSuchObjectException {
        if (!cart.removeIf(book -> book.getBookId().equals(bookId)))
            throw new NoSuchObjectException("There is no book with given ID in cart");
    }

    //return list of books which are unavailable for that user, if list is empty then operation succeeded
    public List<Book> confirmOrder(String email) throws IllegalArgumentException, UsernameNotFoundException{
        if (!userRepository.existsByEmail(email))
            throw new UsernameNotFoundException(email);
        List<Book> unavailableBooks = cart.stream().filter(b -> booksBorrowed.contains(b)).toList();
        if (unavailableBooks.isEmpty()) {
            User user = userRepository.findByEmail(email).get();
            Order order = new Order(user,cart);
            orderRepository.save(order);
            cart.forEach(book -> bookRepository.decrementBookAmountForBookWithId(book.getBookId()));
            clearCart();
            loadBorrowedBooks(email);
        }
        return unavailableBooks;
    }

    public List<Book> getCart() { return cart; }
    public void clearCart(){
        cart.clear();
    }
    public List<Book> getBooksBorrowed() {return  booksBorrowed; }

    public void loadBorrowedBooks(String email){
        if(!userRepository.existsByEmail(email))
            return;
        Long userId = userRepository.findByEmail(email).get().getId();
        List<Order> orders = orderRepository.findAllByUserId(userId);
        booksBorrowed = orders.stream().flatMap(o -> o.getBooksBorrowed().entrySet().stream().filter(entry -> !entry.getValue())).map(Map.Entry::getKey).toList();
    }

    public boolean removeFromBorrowedBooks(Long bookId, String email){
        if(!userRepository.existsByEmail(email))
            return false;
        Long userId = userRepository.findByEmail(email).get().getId();
        if(booksBorrowed.stream().anyMatch(b -> b.getBookId().equals(bookId))){
            bookRepository.incrementBookAmountForBookWithId(bookId);
            Long orderId = orderRepository.findAllByUserId(userId).stream()
                    .filter(o -> o.getBooksBorrowed().entrySet().stream().anyMatch(entry -> entry.getKey().getBookId().equals(bookId) && !entry.getValue()))
                    .findFirst().get().getOrderId();
            orderRepository.updateBorrowedBookStatus(bookId,orderId);
            loadBorrowedBooks(email);
            return true;
        }
        return false;
    }
}