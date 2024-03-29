package com.example.library.service;

import com.example.library.exceptions.AlreadyExistException;
import com.example.library.exceptions.IncorrectParameterValueException;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import com.sun.jdi.ObjectCollectedException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void saveBook(Book book) throws AlreadyExistException {
        book.setCurrentBookAmount(book.getTotalBookAmount());
        //we have to check title, publication year and author in order not to create new entry for same books
        if (bookRepository.existsByTitleAndPublicationYearAndAuthor(book.getTitle(), book.getPublicationYear(), book.getAuthor()))
            throw new AlreadyExistException();
        bookRepository.save(book);
    }

    public void updateBookAmount(Long bookId, int newTotalBookAmount) {
        Book book = bookRepository.findById(bookId).orElseThrow(ObjectCollectedException::new);
        if ((book.getTotalBookAmount() - book.getCurrentBookAmount()) > newTotalBookAmount) {
            throw new IncorrectParameterValueException();
        }
        bookRepository.updateBookAmount(bookId, newTotalBookAmount, newTotalBookAmount - (book.getTotalBookAmount() - book.getCurrentBookAmount()));
    }
}