package com.codegym.repository;

import com.codegym.model.BlogImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogImgRepository extends JpaRepository<BlogImg, Long> {
}
