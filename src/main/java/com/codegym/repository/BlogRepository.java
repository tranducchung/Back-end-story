package com.codegym.repository;

import com.codegym.model.Blog;
import com.codegym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("SELECT b FROM Blog b ORDER BY b.id DESC ")
    List<Blog> findAllByIdOrderById();
    @Query("SELECT b FROM Blog b JOIN b.user u WHERE u.id=?1 order by b.id desc")
    List<Blog> findAllByUserIdAndOrderByIdDesc(Long id);

    Blog findByIdAndUser(Long id, User user);

    Optional<Blog> findAllByTitleContaining(String title);
}
