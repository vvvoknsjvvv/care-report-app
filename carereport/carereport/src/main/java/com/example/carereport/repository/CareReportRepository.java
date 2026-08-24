package com.example.carereport.repository;

import com.example.carereport.entity.CareReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * 事故報告書テーブルのデータベース操作を担当
 */
@Repository
public interface CareReportRepository extends JpaRepository<CareReport, Long> ,JpaSpecificationExecutor<CareReport>{
    List<CareReport> findTop5ByOrderByIncidentDateDesc();
}