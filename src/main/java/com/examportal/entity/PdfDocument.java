package com.examportal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "pdf_documents")
public class PdfDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recordId;        // linked ExamRecord.id (nullable - can be a general document)

    private String studentId;     // owner, so students only see their own PDFs

    private String docType;       // "Hall Ticket", "Result", "Timetable", "Other"

    private String originalFilename;

    // Actual PDF bytes, stored directly in Postgres (bytea) so files survive
    // app restarts/redeploys instead of relying on a local ./uploads folder.
    // @JdbcTypeCode(SqlTypes.VARBINARY) forces Hibernate to bind this as raw
    // bytea. Without it (e.g. plain @Lob), Hibernate 6 maps byte[] to
    // Postgres's large-object (oid) mechanism instead, which sends the value
    // as a bigint OID reference -> "column data is of type bytea but
    // expression is of type bigint" at insert time.
    // @JsonIgnore keeps this out of list endpoints (/api/pdfs, /admin/all) -
    // it's only ever pulled out explicitly for the /{id}/view endpoint.
    @JdbcTypeCode(SqlTypes.VARBINARY)
    @JsonIgnore
    @Column(name = "data", columnDefinition = "bytea")
    private byte[] data;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getDocType() { return docType; }
    public void setDocType(String docType) { this.docType = docType; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
