package com.example.library.controller;

import com.example.library.exceptions.AlreadyExistException;
import com.example.library.exceptions.IncorrectParameterValueException;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import com.sun.jdi.ObjectCollectedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("book-manager")
public class BookController {
    private final BookService bookService;
    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @RequestMapping("/")
    public String mainPage() {

        return "bookController/main-menu";
    }

    @RequestMapping("/addBookForm")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "bookController/add-book-form";
    }

    @PostMapping("/addBook")
    public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "bookController/add-book-form";
        try {
            bookService.saveBook(book);
        } catch (AlreadyExistException e) {
            model.addAttribute("message", "That book with already exist");
            model.addAttribute("changeAmount", "true");
            return "bookController/add-book-form";
        } catch (Exception e) {
            model.addAttribute("message", "Unknown error occurred, contact with system administrator");
            return "bookController/add-book-form";
        }
        model.addAttribute("message", "Book successfully added");
        return "bookController/add-book-form"; //will be developed: redirect to viewBooks and show bolded row with that book
    }

    @GetMapping("/setBookAmountForm")
    public String changeBookAmountMenu(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "bookController/set-book-amount";
    }

    @PostMapping("/setBookAmount")
    public String setBookAmount(@RequestParam(value = "book_id", required = false, defaultValue = "0") Long id, @RequestParam(value = "bookAmount", required = false, defaultValue = "-1") int newTotalBookAmount, Model model) {
        boolean errors = false;
        try {
            bookService.updateBookAmount(id, newTotalBookAmount);
        } catch (IncorrectParameterValueException e) {
            model.addAttribute("errorValueMessage", "New total amount of books has to be positive integer and cannot be lower than books which has been borrowed");
            errors = true;
        } catch (ObjectCollectedException e) {
            model.addAttribute("errorIdMessage", "Could find book with given ID");
            errors = true;
        }
        if (!errors) model.addAttribute("successMessage", "Book amount changed successfully");
        model.addAttribute("books", bookRepository.findAll());
        return "bookController/set-book-amount";
    }

    @GetMapping("/viewBooks")
    public String viewBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "bookController/view-books";
    }
}