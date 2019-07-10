package com.codegym.repository;

import com.codegym.model.MyUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyUploadRepository extends JpaRepository<MyUpload, Long> {
   // List<MyUpload> findAllByUserId(Long id);
}
