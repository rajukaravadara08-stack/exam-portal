package com.examportal.controller;

import com.examportal.entity.ExamRecord;
import com.examportal.repository.ExamRecordRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final ExamRecordRepository recordRepo;

    public RecordController(ExamRecordRepository recordRepo) {
        this.recordRepo = recordRepo;
    }

    /** Student view: only the logged-in student's own exam records. */
    @GetMapping
    @Transactional
    public List<ExamRecord> myRecords(Authentication auth) {
        String userId = auth.getName();
        return recordRepo.findByStudentId(userId);
    }

    // ---------- Admin endpoints ----------

    @GetMapping("/admin/all")
    public List<ExamRecord> allRecords() {
        return recordRepo.findAll();
    }

    @GetMapping("/admin/student/{studentId}")
    public List<ExamRecord> byStudent(@PathVariable String studentId) {
        return recordRepo.findByStudentId(studentId);
    }

    @PostMapping("/admin")
    public ExamRecord create(@Valid @RequestBody ExamRecord record) {
        record.setId(null);
        return recordRepo.save(record);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ExamRecord updated) {
        return recordRepo.findById(id).map(existing -> {
            updated.setId(id);
            return ResponseEntity.ok(recordRepo.save(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!recordRepo.existsById(id)) return ResponseEntity.notFound().build();
        recordRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
