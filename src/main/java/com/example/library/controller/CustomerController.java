package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import com.example.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.rmi.NoSuchObjectException;
import java.util.List;

@Controller
@RequestMapping("library")
public class CustomerController {
    private final CustomerService customerService;
    private final BookRepository bookRepository;

    private String lastSortedBy = "title";

    private Sort.Direction lastSortDirection = Sort.Direction.ASC;

    @Autowired
    public CustomerController(CustomerService customerService, BookRepository bookRepository) {
        this.customerService = customerService;
        this.bookRepository = bookRepository;
    }

    @RequestMapping("/")
    public String displayMainMenu() {
        return "customerController/main-menu";
    }

    @RequestMapping("/viewBooks")
    public String displayBooksView(@RequestParam(value = "sort_by", required = false, defaultValue = "title") String sortBy, Model model) {
//        if previous sort by parameter is the same as current one  then program changes sort order
//        if previous sort by parameter is different from the current one when we sort by current parameter with ascending order
        if (lastSortedBy.equals(sortBy)) {
            if (lastSortDirection.equals(Sort.Direction.ASC)) lastSortDirection = Sort.Direction.DESC;
            else lastSortDirection = Sort.Direction.ASC;
        } else lastSortDirection = Sort.Direction.ASC;
        model.addAttribute("books", bookRepository.findAllByCurrentBookAmountGreaterThan(0, Sort.by(lastSortDirection, sortBy)));
        lastSortedBy = sortBy;
        return "customerController/view-books";
    }

    @PostMapping("/addToCart")
    public String addToCartBookWithIdFromBookView(@RequestParam(value = "book_id", required = false, defaultValue = "0") Long bookId, Model model) {
        try {
            customerService.addToCart(bookId);
            model.addAttribute("successMessage", "Book has been successfully added to cart. Please note that books in cart are not reserved");
        } catch (NoSuchObjectException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("books", bookRepository.findAllByCurrentBookAmountGreaterThan(0, Sort.by(lastSortDirection, lastSortedBy)));
        return "customerController/view-books";
    }

    @GetMapping("/cartView")
    public String displayCart(Model model) {
        model.addAttribute("booksInCart", customerService.getCart());
        return "customerController/view-cart";
    }

    @PostMapping("/removeFromCart")
    public String deleteFromCartById(@RequestParam(value = "book_id", required = false, defaultValue = "0") Long bookId, Model model) {
        try {
            customerService.removeFromCart(bookId);
            model.addAttribute("successMessage", "Book with given ID has been removed");
        } catch (NoSuchObjectException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("booksInCart", customerService.getCart());
        return "customerController/view-cart";
    }

    @GetMapping("/searchForBookForm")
    public String displaySearchForm() {
        return "customerController/search-for-book";
    }

    @PostMapping("/searchForBook")
    public String searchForBooks(@RequestParam(value = "title", required = false, defaultValue = "21372137") String title, Model model) {
        List<Book> availableBooks = bookRepository.findAllByTitleIsContainingIgnoreCaseAndCurrentBookAmountGreaterThan(title, 0);
        if (availableBooks.isEmpty()) {
            model.addAttribute("notFoundMessage", "Could not find book with given title");
        } else {
            model.addAttribute("books", availableBooks);
        }
        model.addAttribute("title", title);
        return "customerController/search-for-book";
    }

    @PostMapping("/addToCartFromSearchView")
    public String addToCartFromSearchPage(@RequestParam(value = "book_id", required = false, defaultValue = "0") Long bookId, Model model) {
        try {
            customerService.addToCart(bookId);
            model.addAttribute("successMessage", "Book added to cart successfully");
        } catch (NoSuchObjectException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "customerController/search-for-book";
    }

    @PostMapping("/confirmOrder")
    public String confirmOrder(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try{
            List <Book>unavailableBooks = customerService.confirmOrder(email);
            if (unavailableBooks.isEmpty())
                model.addAttribute("orderMessage", "Order confirmed. We wish you good reading.");
            else
                model.addAttribute("unavailableBooks",  unavailableBooks.stream().map(book -> book.getTitle()));
        }
        catch (UsernameNotFoundException e){
            model.addAttribute("orderMessage", e.getMessage());
        }
        model.addAttribute("booksInCart", customerService.getCart());
        return "/customerController/view-cart";
    }

    @GetMapping("/returnBookForm")
    public String returnBooksPage(Model model){
        model.addAttribute("borrowedBooks", customerService.getBooksBorrowed());
        return "/customerController/return-books";
    }

    @PostMapping("/returnBook")
    public String returnBook(@RequestParam(value = "book_id", required = false, defaultValue = "0") Long bookId, Model model){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (customerService.removeFromBorrowedBooks(bookId,email)){
            model.addAttribute("successMessage", "Book with given ID has been removed successfully");
        }
        else{
            model.addAttribute("errorMessage", "Please try again with valid book ID");
        }
        model.addAttribute("borrowedBooks", customerService.getBooksBorrowed());
        return "/customerController/return-books";
    }
}