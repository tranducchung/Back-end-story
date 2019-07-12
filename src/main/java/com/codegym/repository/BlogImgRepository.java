package com.codegym.repository;

import com.codegym.model.BlogImg;
import com.codegym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogImgRepository extends JpaRepository<BlogImg, Long> {
    List<BlogImg> findAllByUser(User user);

    BlogImg findByIdAndUser(Long id, User user);
}
