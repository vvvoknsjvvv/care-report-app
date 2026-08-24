package com.example.carereport.repository;

import com.example.carereport.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 利用者（patients）テーブルのデータベース操作を担当
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}