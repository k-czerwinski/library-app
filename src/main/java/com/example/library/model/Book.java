package com.example.library.model;

import com.example.library.model.enums.BookGenre;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Objects;

@Entity
@Table(name = "books")
@SecondaryTable(name = "books_collection", pkJoinColumns = @PrimaryKeyJoinColumn(name = "book_id"))
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Enumerated(EnumType.STRING)
    private BookGenre bookGenre;
    @NotEmpty(message = "You have to fill book title field")
    @Size(min = 3, max = 50)
    private String title;

    @Min(1900)
    private int publicationYear;

    @NotEmpty(message = "Not null")
    @Size(min = 2, max = 40)
    private String author;

    @Column(table = "books_collection", name = "current_amount")
    @Min(0)
    private int currentBookAmount;

    @Column(table = "books_collection", name = "total_amount")
    @Min(0)
    private int totalBookAmount;

    public int getTotalBookAmount() {
        return totalBookAmount;
    }

    public void setTotalBookAmount(int totalBookAmount) {
        this.totalBookAmount = totalBookAmount;
    }

    public int getCurrentBookAmount() {
        return currentBookAmount;
    }

    public void setCurrentBookAmount(int currentBookAmount) {
        this.currentBookAmount = currentBookAmount;
    }

    public Book() {
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public BookGenre getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(BookGenre bookGenre) {
        this.bookGenre = bookGenre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return publicationYear == book.publicationYear && bookId.equals(book.bookId) && bookGenre == book.bookGenre && title.equals(book.title) && author.equals(book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, bookGenre, title, publicationYear, author);
    }
}