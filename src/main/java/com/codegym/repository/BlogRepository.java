package com.codegym.repository;

import com.codegym.model.Blog;
import com.codegym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("SELECT b FROM Blog b ORDER BY b.id DESC ")
    List<Blog> findAllByIdOrderById();
    @Query("SELECT b FROM Blog b JOIN b.user u WHERE u.id=?1 order by b.id desc")
    List<Blog> findAllByUserIdAndOrderByIdDesc(Long id);

    Blog findByIdAndUser(Long id, User user);
}
