package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.Order;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.OrderRepository;
import com.example.library.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    private final UserRepository userRepository;
    private List<Book> cart = new ArrayList<Book>();

    private List<Book> booksBorrowed = new ArrayList<>();

    @Autowired
    public CustomerService(OrderRepository orderRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public void addToCart(Long bookId) throws NoSuchObjectException {
        if (!bookRepository.existsById(bookId)) throw new NoSuchObjectException("Book with given ID not exists");
        Book book = bookRepository.findById(bookId).get();
        if (book.getCurrentBookAmount() < 1) throw new NoSuchObjectException("Book with given ID is not available");
        if (cart.stream().anyMatch(b -> b.getBookId().equals(bookId)))
            throw new NoSuchObjectException("You cannot borrow several of the same book");
        cart.add(book);
    }

    public void removeFromCart(Long bookId) throws NoSuchObjectException {
        if (!cart.removeIf(book -> book.getBookId().equals(bookId)))
            throw new NoSuchObjectException("There is no book with given ID in cart");
    }

    public boolean confirmOrder(String email) throws IllegalArgumentException{
        if (!userRepository.existsByEmail(email))
            return false;
        List<Book> booksBorrowedBefore = cart.stream().filter(b -> booksBorrowed.contains(b)).toList();
        if (!booksBorrowedBefore.isEmpty()){
            throw new IllegalArgumentException("You can't borrow more than one piece of book. Books which you borrowed before and wasn't returned: "
                    + booksBorrowedBefore.stream().map(Book::getTitle).reduce((o1,o2) -> o1+", "+o2 ).get());
        }
        List<Book> unavailableBooks = cart.stream().filter(book -> bookRepository.findById(book.getBookId()).get().getCurrentBookAmount() <= 0).toList();
        if (unavailableBooks.isEmpty()) {
            User user = userRepository.findByEmail(email);
            Order order = new Order(user,cart);
            orderRepository.save(order);
            cart.forEach(book -> bookRepository.decrementBookAmountForBookWithId(book.getBookId()));
            cart.clear();
            loadBorrowedBooks(email);
            return true;
        }
        unavailableBooks.forEach(book -> cart.removeIf(b -> b.getBookId().equals(book.getBookId())));
        return false;
    }

    public List<Book> getCart() { return cart; }
    public void clearCart(){
        cart.clear();
    }
    public List<Book> getBooksBorrowed() {return  booksBorrowed; }
    public void clearBooksBorrowed() { booksBorrowed.clear(); }

    public void loadBorrowedBooks(String email){
        Long userId = userRepository.findByEmail(email).getId();
        List<Order> orders = orderRepository.findAllByUserId(userId);
        booksBorrowed = orders.stream().flatMap(o -> o.getBooksBorrowed().entrySet().stream().filter(entry -> entry.getValue() == false)).map(e -> e.getKey()).toList();
    }

    public boolean removeFromBorrowedBooks(Long bookId, String email){
        if(!userRepository.existsByEmail(email))
            return false;
        Long userId = userRepository.findByEmail(email).getId();
        if(booksBorrowed.stream().anyMatch(b -> b.getBookId().equals(bookId))){
            bookRepository.incrementBookAmountForBookWithId(bookId);
            Long orderId = orderRepository.findAllByUserId(userId).stream()
                    .filter(o -> o.getBooksBorrowed().entrySet().stream().anyMatch(entry -> entry.getKey().getBookId().equals(bookId) && entry.getValue() == false))
                    .findFirst().get().getOrderId();
            orderRepository.updateBorrowedBookStatus(bookId,orderId);
            loadBorrowedBooks(email);
            return true;
        }
        return false;
    }
}