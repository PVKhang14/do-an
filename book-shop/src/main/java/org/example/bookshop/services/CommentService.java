package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.comment.CommentDto;
import org.example.bookshop.entities.Book;
import org.example.bookshop.entities.Comment;
import org.example.bookshop.entities.Rating;
import org.example.bookshop.entities.User;
import org.example.bookshop.exceptions.DataNotFoundException;
import org.example.bookshop.exceptions.ResourceAlreadyExisted;
import org.example.bookshop.repositories.IBookRepository;
import org.example.bookshop.repositories.ICommentRepository;
import org.example.bookshop.repositories.IOrderDetailRepository;
import org.example.bookshop.repositories.IRatingRepository;
import org.example.bookshop.responses.comment.CommentResponse;
import org.example.bookshop.responses.comment.RatingPercentageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ICommentRepository commentRepository;
    private final IRatingRepository ratingRepository;
    private final IBookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final IOrderDetailRepository orderDetailRepository;

    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentByBookId(Integer bookId, Pageable pageable) {
        Page<CommentResponse> commentResponses = commentRepository.findAllCommentByBookID(bookId, pageable);

        return commentResponses;
    }

    @Transactional(readOnly = true)
    public List<RatingPercentageResponse> calculateRatingPercentage(Integer bookID) {
        long totalRatings = ratingRepository.countByBookID_Id(bookID);

        List<RatingPercentageResponse> ratingCounts = ratingRepository.countRatingByBookID(bookID);

        ratingCounts.forEach(response -> response.setPercentage((response.getCount() * 100.0) / totalRatings));

        return ratingCounts;
    }

    private void updateAverageRating(Integer bookID) {
        Book reCalculateBook = bookRepository.findById(bookID)
                .orElseThrow(() -> new DataNotFoundException("Book not found with ID: " + bookID));

        List<Rating> ratings = ratingRepository.findAllByBookID_Id(bookID);

        BigDecimal avgRatingByBook = ratings.stream()
                .map(rating -> BigDecimal.valueOf(rating.getRating()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(ratings.size()), 1, RoundingMode.HALF_UP);

        reCalculateBook.setAverageRating(avgRatingByBook);

        bookRepository.save(reCalculateBook);
    }

    @Transactional(readOnly = true)
    public boolean isEnableComment(Integer bookID, Integer userID) {
        if (userID == null) {
            return false;
        }

        long count = orderDetailRepository.countBooksPurchasedByUser(userID, bookID);
        boolean existed = commentRepository.existsByUserID_IdAndBookID_Id(userID, bookID);

        return count > 0 && !existed;

    }

    @Transactional
    public Map<String, Object> saveComment(CommentDto commentDto) {
        long count = orderDetailRepository.countBooksPurchasedByUser(commentDto.getUserId(), commentDto.getBookId());

        if (count == 0) {
            throw new DataNotFoundException("User has not purchased this book");
        }

        boolean existed = commentRepository.existsByUserID_IdAndBookID_Id(commentDto.getUserId(), commentDto.getBookId());

        if (existed) {
            throw new ResourceAlreadyExisted("User has commented this book");
        }

        Comment comment = Comment.builder()
                .commentDate(LocalDateTime.now())
                .content(commentDto.getContent())
                .bookID(bookRepository.findById(commentDto.getBookId())
                        .orElseThrow(() -> new DataNotFoundException("Book not found with ID: " + commentDto.getBookId())))
                .userID(User.builder()
                        .id(commentDto.getUserId())
                        .build()
                )

                .build();

        Rating rating = Rating.builder()
                .bookID(comment.getBookID())
                .userID(comment.getUserID())
                .rating(commentDto.getRating())
                .ratingDate(LocalDateTime.now())
                .build();

        ratingRepository.save(rating);

        commentRepository.save(comment);

        this.updateAverageRating(commentDto.getBookId());

        return Map.of(
                "content", commentDto.getContent(),
                "rating", commentDto.getRating(),
                "userId", commentDto.getUserId()
        );
    }


}
