package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.Order;
import com.example.library.model.User;
import com.example.library.model.enums.UserRole;
import com.example.library.repository.BookRepository;
import com.example.library.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerService {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private List<Book> cart = new ArrayList<Book>();

    @Autowired
    public CustomerService(OrderRepository orderRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
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

    public boolean confirmOrder() {
        List<Book> unavailableBooks = cart.stream().filter(book -> bookRepository.findById(book.getBookId()).get().getCurrentBookAmount() <= 0).toList();
        if (unavailableBooks.isEmpty()) {
//            ultimately user will be saved as user which is currently signed in
            User user = new User(UserRole.CUSTOMER, "AAAA","BBBB","CCCC","PASSWD");
            Order order = new Order(user,cart);
            orderRepository.save(order);
            cart.forEach(book -> bookRepository.decrementBookAmountForBookWithId(book.getBookId()));
            cart.clear();
            return true;
        }
        unavailableBooks.forEach(book -> cart.removeIf(b -> b.getBookId() == book.getBookId()));
        return false;
    }

    public List<Book> getCart() {
        return cart;
    }
}
