package com.examportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_records")
public class ExamRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;     // FK-ish reference to AppUser.userId (role STUDENT)

    private String semesterName;
    private String academicYear;
    private String examEvent;
    private String facultyName = "Faculty of Arts";
    private String appearanceType;   // Fresher / Repeater
    private String formNo;
    private String examFeeAmount;
    private String examFeeStartDate;
    private String examFeeEndDate;
    private String inward = "Inwarded";
    private String seatNumber;
    private String resultStatus;     // Complete / Incomplete

    private String sgpa;

    @Column(columnDefinition = "TEXT")
    private String subjectsJson;     // JSON array of [subject, marks] pairs

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSemesterName() { return semesterName; }
    public void setSemesterName(String semesterName) { this.semesterName = semesterName; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public String getExamEvent() { return examEvent; }
    public void setExamEvent(String examEvent) { this.examEvent = examEvent; }

    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }

    public String getAppearanceType() { return appearanceType; }
    public void setAppearanceType(String appearanceType) { this.appearanceType = appearanceType; }

    public String getFormNo() { return formNo; }
    public void setFormNo(String formNo) { this.formNo = formNo; }

    public String getExamFeeAmount() { return examFeeAmount; }
    public void setExamFeeAmount(String examFeeAmount) { this.examFeeAmount = examFeeAmount; }

    public String getExamFeeStartDate() { return examFeeStartDate; }
    public void setExamFeeStartDate(String examFeeStartDate) { this.examFeeStartDate = examFeeStartDate; }

    public String getExamFeeEndDate() { return examFeeEndDate; }
    public void setExamFeeEndDate(String examFeeEndDate) { this.examFeeEndDate = examFeeEndDate; }

    public String getInward() { return inward; }
    public void setInward(String inward) { this.inward = inward; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public String getResultStatus() { return resultStatus; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }

    public String getSgpa() { return sgpa; }
    public void setSgpa(String sgpa) { this.sgpa = sgpa; }

    public String getSubjectsJson() { return subjectsJson; }
    public void setSubjectsJson(String subjectsJson) { this.subjectsJson = subjectsJson; }
}
