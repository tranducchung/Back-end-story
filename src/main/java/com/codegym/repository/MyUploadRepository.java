package com.codegym.repository;

import com.codegym.model.MyUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyUploadRepository extends JpaRepository<MyUpload, Long> {
    List<MyUpload> findAllByUserId(Long id);
}
