package com.example.carereport.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDate;

/**
 * 利用者（patients）テーブルの設計図（Entity）
 */
@Entity
@Table(name = "patients")
public class Patient {

    // 主キー（PK）で、自動連番になる設定
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 画面の入力項目に対応するカラム
    private String patientName;    // 利用者氏名
    private LocalDate birthDate;   // 生年月日
    private String careLevel;      // 要介護度
    private String medicalHistory; // 既往歴
    private String emergencyContact; //緊急連絡先
    // =========================================
    // Getter と Setter
    // =========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCareLevel() {
        return careLevel;
    }

    public void setCareLevel(String careLevel) {
        this.careLevel = careLevel;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getEmergencyContact(){
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
}