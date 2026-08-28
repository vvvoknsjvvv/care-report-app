package com.example.carereport.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 事故報告書（care_reports）テーブルの設計図
 */
@Entity
@Table(name = "care_reports")
public class CareReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 画面の入力項目に対応するカラム

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "created_by_user_id")
    private UserAccount createdByUser; //作成者（UserAccountと紐づけ）
    private Long patientId;             // 対象利用者のID
    private LocalDateTime incidentDate; // 発生日時
    
    private Integer locationId;         // 発生場所（ラジオボタンの値）
    private String locationOther;       // 発生場所（その他テキスト）
    
    private Integer categoryId;         // 事故カテゴリー（ラジオボタンの値）
    private String categoryOther;       // 事故カテゴリー（その他テキスト）
    
    private Integer medicalActionFlag;  // 医療的対応（0:なし, 1:あり）
    private String medicalAction;       // 処置内容
    
    private String situationDesc;       // 事故の発生状況
    private String responseDesc;        // 事故発生時の対応
    private String patientCondition;    // 事故発生後の利用者の状況
    @javax.persistence.Column(columnDefinition = "TEXT")
    private String familyReport; //家族への報告
    @javax.persistence.Column(columnDefinition = "TEXT")
    private String causeAnalysis; //事故の原因分析
    @javax.persistence.Column(columnDefinition = "TEXT")
    private String preventiveMeasure; //再発防止策
    @Column(name = "manager_check")
    private Boolean managerCheck; // 施設長への報告
    @Column(name = "complete_check")
    private Boolean completeCheck; // 完了確認


    @javax.persistence.Transient // データベースのカラムとして扱わない（画面表示用）
    private String patientName;
    @javax.persistence.Transient
    private String categoryName;

    @javax.persistence.Transient
    private String locationName;

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }


    // =========================================
    // Getter と Setter
    // =========================================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public LocalDateTime getIncidentDate() { return incidentDate; }
    public void setIncidentDate(LocalDateTime incidentDate) { this.incidentDate = incidentDate; }

    public Integer getLocationId() { return locationId; }
    public void setLocationId(Integer locationId) { this.locationId = locationId; }

    public String getLocationOther() { return locationOther; }
    public void setLocationOther(String locationOther) { this.locationOther = locationOther; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getCategoryOther() { return categoryOther; }
    public void setCategoryOther(String categoryOther) { this.categoryOther = categoryOther; }

    public Integer getMedicalActionFlag() { return medicalActionFlag; }
    public void setMedicalActionFlag(Integer medicalActionFlag) { this.medicalActionFlag = medicalActionFlag; }

    public String getMedicalAction() { return medicalAction; }
    public void setMedicalAction(String medicalAction) { this.medicalAction = medicalAction; }

    public String getSituationDesc() { return situationDesc; }
    public void setSituationDesc(String situationDesc) { this.situationDesc = situationDesc; }

    public String getResponseDesc() { return responseDesc; }
    public void setResponseDesc(String responseDesc) { this.responseDesc = responseDesc; }

    public String getPatientCondition() { return patientCondition; }
    public void setPatientCondition(String patientCondition) { this.patientCondition = patientCondition; }

    public UserAccount getCreatedByUser() { return createdByUser; }
    public void setCreatedByUser(UserAccount createdByUser) { this.createdByUser = createdByUser; }

    public String getFamilyReport() { return familyReport; }
    public void setFamilyReport(String familyReport) { this.familyReport = familyReport; }

    public String getCauseAnalysis() { return causeAnalysis; }
    public void setCauseAnalysis(String causeAnalysis) { this.causeAnalysis = causeAnalysis; }

    public String getPreventiveMeasure() { return preventiveMeasure; }
    public void setPreventiveMeasure(String preventiveMeasure) { this.preventiveMeasure = preventiveMeasure; }
    public Boolean getManagerCheck() {
        return managerCheck;
    }
    public void setManagerCheck(Boolean managerCheck) {
        this.managerCheck = managerCheck;
    }
    public Boolean getCompleteCheck() {
        return completeCheck;
    }
    public void setCompleteCheck(Boolean completeCheck) {
        this.completeCheck = completeCheck;
    }

    // 名前表示用
    @javax.persistence.Transient 
    private String builderName;

    public String getBuilderName() { return builderName; }
    public void setBuilderName(String builderName) { this.builderName = builderName; }

}