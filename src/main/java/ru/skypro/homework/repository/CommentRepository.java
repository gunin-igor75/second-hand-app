package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByAds_Id(int id);

    void deleteCommentByAds_Id(int adsId);

    Optional<Comment> findCommentByIdAndUsersId(int commentId, int usersId);

}
