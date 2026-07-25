package com.examportal.repository;

import com.examportal.entity.PdfDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PdfDocumentRepository extends JpaRepository<PdfDocument, Long> {
    List<PdfDocument> findByStudentId(String studentId);
    List<PdfDocument> findByRecordId(Long recordId);
}
