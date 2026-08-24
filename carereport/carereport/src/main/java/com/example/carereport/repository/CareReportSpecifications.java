package com.example.carereport.repository;

import com.example.carereport.entity.CareReport;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CareReportSpecifications {

    public static Specification<CareReport> search(
            Long patientId, LocalDate startDate, LocalDate endDate,
            Integer locationId, Integer categoryId, String keyword) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. 利用者別
            if (patientId != null) {
                predicates.add(cb.equal(root.get("patientId"), patientId));
            }
            // 2. 発生日（いつから：その日の0時0分以降）
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("incidentDate"), startDate.atStartOfDay()));
            }
            // 3. 発生日（いつまで：その日の23時59分以前）
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("incidentDate"), endDate.atTime(LocalTime.MAX)));
            }
            // 4. 発生場所
            if (locationId != null) {
                predicates.add(cb.equal(root.get("locationId"), locationId));
            }
            // 5. 事故カテゴリー
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("categoryId"), categoryId));
            }
            // 6. フリーワード（状況 or 対応 or 状態のどこかに文字が含まれていればOK）
            if (StringUtils.hasText(keyword)) {
                String likeKeyword = "%" + keyword + "%";
                Predicate sitDesc = cb.like(root.get("situationDesc"), likeKeyword);
                Predicate resDesc = cb.like(root.get("responseDesc"), likeKeyword);
                Predicate patCond = cb.like(root.get("patientCondition"), likeKeyword);
                predicates.add(cb.or(sitDesc, resDesc, patCond)); // ORで繋ぐ
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}