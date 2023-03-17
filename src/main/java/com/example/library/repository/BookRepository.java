package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleAndPublicationYearAndAuthor(String title, int publicationYear, String author);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Book b set b.currentBookAmount = b.currentBookAmount - 1 WHERE b.bookId = :bookId")
    void decrementBookAmountForBookWithId(@Param(value = "bookId") Long id);

    List<Book> findAllByTitleIsContainingIgnoreCaseAndCurrentBookAmountGreaterThan(String title, int currentBookAmount);

    List<Book> findAllByCurrentBookAmountGreaterThan(int currentBookAmount, Sort sort);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Book b set b.totalBookAmount = :totalAmount, b.currentBookAmount = :currentAmount WHERE b.bookId = :bookId")
    void updateBookAmount(@Param(value = "bookId") Long id, @Param(value = "totalAmount") int totalAmount, @Param(value = "currentAmount") int currentAmount);
}