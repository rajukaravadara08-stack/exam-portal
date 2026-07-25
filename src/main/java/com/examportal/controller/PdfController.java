package com.examportal.controller;

import com.examportal.entity.PdfDocument;
import com.examportal.repository.PdfDocumentRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pdfs")
public class PdfController {

    private final PdfDocumentRepository pdfRepo;

    public PdfController(PdfDocumentRepository pdfRepo) {
        this.pdfRepo = pdfRepo;
    }

    /** Student view: only their own documents. */
    @GetMapping
    public List<PdfDocument> myDocs(Authentication auth) {
        return pdfRepo.findByStudentId(auth.getName());
    }

    @GetMapping("/admin/all")
    public List<PdfDocument> allDocs() {
        return pdfRepo.findAll();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                     @RequestParam("studentId") String studentId,
                                     @RequestParam("docType") String docType,
                                     @RequestParam(value = "recordId", required = false) Long recordId) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No file provided."));
        }
        if (!"application/pdf".equals(file.getContentType())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only PDF files are allowed."));
        }

        PdfDocument doc = new PdfDocument();
        doc.setStudentId(studentId);
        doc.setDocType(docType);
        doc.setRecordId(recordId);
        doc.setOriginalFilename(file.getOriginalFilename());
        doc.setData(file.getBytes());
        pdfRepo.save(doc);

        return ResponseEntity.ok(doc);
    }

    /** Replace the file behind an existing PDF record (admin update). */
    @PutMapping("/{id}/replace")
    public ResponseEntity<?> replace(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        var docOpt = pdfRepo.findById(id);
        if (docOpt.isEmpty()) return ResponseEntity.notFound().build();
        if (!"application/pdf".equals(file.getContentType())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only PDF files are allowed."));
        }

        PdfDocument doc = docOpt.get();
        doc.setData(file.getBytes());
        doc.setOriginalFilename(file.getOriginalFilename());
        pdfRepo.save(doc);

        return ResponseEntity.ok(doc);
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<byte[]> view(@PathVariable Long id, Authentication auth) {
        var docOpt = pdfRepo.findById(id);
        if (docOpt.isEmpty()) return ResponseEntity.notFound().build();
        PdfDocument doc = docOpt.get();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !doc.getStudentId().equals(auth.getName())) {
            return ResponseEntity.status(403).build();
        }

        if (doc.getData() == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getOriginalFilename() + "\"")
                .body(doc.getData());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var docOpt = pdfRepo.findById(id);
        if (docOpt.isEmpty()) return ResponseEntity.notFound().build();

        pdfRepo.delete(docOpt.get());

        return ResponseEntity.noContent().build();
    }
}
