package org.example.bookshop.repositories;

import org.example.bookshop.entities.Book;
import org.example.bookshop.entities.Category;
import org.example.bookshop.responses.book.BookResponse;
import org.example.bookshop.responses.book.PublisherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


public interface IBookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @Query("""
                Select
                new org.example.bookshop.responses.book.BookResponse(
                b.id,
                 b.title,
                 b.author,
                 b.price,
                 b.quantity,
                 b.categoryID.categoryName,
                 b.description,
                 b.publisher,
                 b.publishedDate,
                 b.image,
                 b.averageRating
                 )
                From Book b
            """)
    Page<BookResponse> findAllBook(Pageable pageable);

    Page<Book> findBooksByCategoryID(Category categoryID, Pageable pageable);

    @Query("SELECT b.averageRating FROM Book b WHERE b.id = :bookID")
    BigDecimal getAverageRatingById(@Param("bookID") Integer bookID);


    @Query("SELECT od.bookID.id, SUM(od.quantity) " +
            "FROM OrderDetail od " +
            "WHERE od.bookID.id IN :bookIds " +
            "GROUP BY od.bookID.id")
    List<Object[]> getPurchaseCountsByBookIds(@Param("bookIds") List<Integer> bookIds);


//    @EntityGraph(attributePaths = {"categoryID"})
//    @Query("SELECT b FROM Book b")
//    Page<Book> findAllWithCategory(Specification<Book> spec, Pageable pageable);


    @EntityGraph(attributePaths = {"categoryID"})
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

    @Query("SELECT b.quantity FROM Book b WHERE b.id = :bookID")
    int getQuantityById(Integer bookID);

    @Query("select SUM(od.quantity) from OrderDetail od where od.bookID.id = :bookID")
    Long getPurchaseCountByBookID(@Param("bookID") Integer bookID);

    @Query("SELECT distinct new org.example.bookshop.responses.book.PublisherResponse(b.publisher) FROM Book b")
    Page<PublisherResponse> findAllPublisher(Pageable pageable);
}