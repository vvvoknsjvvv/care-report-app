package com.example.carereport.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.carereport.entity.CareReport;
import com.example.carereport.entity.Patient;
import com.example.carereport.repository.CareReportRepository;
import com.example.carereport.repository.PatientRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.carereport.repository.UserAccountRepository;
import com.example.carereport.entity.UserAccount;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class CareReportController {

    private final CareReportRepository careReportRepository;
    private final PatientRepository patientRepository; // 利用者データを引っ張るための窓口を追加
    private final UserAccountRepository userAccountRepository;

    // コンストラクタで両方の窓口を受け取る
    public CareReportController(CareReportRepository careReportRepository, PatientRepository patientRepository, UserAccountRepository userAccountRepository) {
        this.careReportRepository = careReportRepository;
        this.patientRepository = patientRepository;
        this.userAccountRepository = userAccountRepository;
    }

    // ---------------------------------------------------------
    // 報告書作成画面を「表示」するときの処理
    // ---------------------------------------------------------
    @GetMapping("/care_report")
    public String showCareReportForm(Model model) {
        // 1. データベースから利用者全員のデータを取得する
        List<Patient> patients = patientRepository.findAll();
        
        // 2. 取得した利用者リストを、"patients"という名前で画面（HTML）に渡す
        model.addAttribute("patients", patients);
        
        // 3. templatesフォルダの中にある care_report.html を表示する
        return "care_report";
    }

    @GetMapping("/report_list")
    public String showReportList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) Long searchPatientId,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchStartDate,
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchEndDate,
        @RequestParam(required = false) Integer searchLocationId,
        @RequestParam(required = false) Integer searchCategoryId,
        @RequestParam(required = false) String searchKeyword, Model model) {

        org.springframework.data.jpa.domain.Specification<CareReport> spec = 
            com.example.carereport.repository.CareReportSpecifications.search(
                searchPatientId, searchStartDate, searchEndDate, searchLocationId, searchCategoryId, searchKeyword);

        // 2. ページネーションとソートの条件
        org.springframework.data.domain.Pageable pageable = 
            org.springframework.data.domain.PageRequest.of(page, 10, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "incidentDate"));
        
        // 3. 検索ルールとページ条件をセットにしてデータベースから取得！
        org.springframework.data.domain.Page<CareReport> reportPage = careReportRepository.findAll(spec, pageable);

        for (CareReport report : reportPage.getContent()) {
            
            Long patientId = report.getPatientId();
            if(patientId != null){
                patientRepository.findById(patientId).ifPresent(patient -> {
                    report.setPatientName(patient.getPatientName());
                });
            }
            if (report.getCategoryId() != null) {
                switch (report.getCategoryId()) {
                    case 1: report.setCategoryName("転倒・転落"); break;
                    case 2: report.setCategoryName("誤嚥・窒息"); break;
                    case 3: report.setCategoryName("皮膚剥離"); break;
                    case 4: report.setCategoryName("誤薬、投薬漏れ"); break;
                    case 5: report.setCategoryName("食中毒・感染症等"); break;
                    case 6: report.setCategoryName("医療処置関連"); break;
                    case 7: report.setCategoryName("異食"); break;
                    case 8: report.setCategoryName("徘徊・行方不明"); break;
                    case 9: report.setCategoryName("職員の法令違反・不祥事"); break;
                    case 10: report.setCategoryName("不明"); break;
                    case 99: report.setCategoryName(report.getCategoryOther()); break; // その他の場合
                    default: report.setCategoryName("不明");
                }
            }

            if (report.getLocationId() != null) {
                switch (report.getLocationId()) {
                    case 1: report.setLocationName("居室"); break;
                    case 2: report.setLocationName("食堂等共用部"); break;
                    case 3: report.setLocationName("機能訓練室"); break;
                    case 4: report.setLocationName("浴室・脱衣"); break;
                    case 5: report.setLocationName("トイレ"); break;
                    case 6: report.setLocationName("廊下"); break;
                    case 7: report.setLocationName("敷地内の建物外"); break;
                    case 8: report.setLocationName("在宅"); break;
                    case 9: report.setLocationName("敷地外"); break;
                    case 99: report.setLocationName(report.getLocationOther()); break; // その他の場合
                    default: report.setLocationName("不明");
                }
            }

            if (report.getCreatedByUser() != null && report.getCreatedByUser().getStaffname() != null) {
                report.setBuilderName(report.getCreatedByUser().getStaffname());
                } else {
                report.setBuilderName("不明");
                }

        }

        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("patients", patients);

        // 6. 入力された検索条件を画面に返し、検索後もフォームに文字が残るようにする
        model.addAttribute("searchPatientId", searchPatientId);
        model.addAttribute("searchStartDate", searchStartDate);
        model.addAttribute("searchEndDate", searchEndDate);
        model.addAttribute("searchLocationId", searchLocationId);
        model.addAttribute("searchCategoryId", searchCategoryId);
        model.addAttribute("searchKeyword", searchKeyword);

        model.addAttribute("reportPage", reportPage);
        
        return "report_list";
    }

    @GetMapping("/home")
    public String showHome(Model model, Principal principal) {
        
        if (principal != null) {
            String loginId = principal.getName(); 
            String staffName = "名称未設定"; 
            UserAccount account = userAccountRepository.findByLoginId(loginId).orElse(null);

            if (account != null && account.getStaffname() != null) {
                staffName = account.getStaffname();
            }
            model.addAttribute("staff_name", staffName);
        }
        

        // 1. 直近5件の報告書を取得する
        List<CareReport> recentReports = careReportRepository.findTop5ByOrderByIncidentDateDesc();

        for (CareReport report : recentReports) {
        Long patientId = report.getPatientId();
        if (patientId != null) {
        patientRepository.findById(report.getPatientId()).ifPresent(patient -> {
            report.setPatientName(patient.getPatientName());
        });
    }

            // カテゴリーIDを文字に翻訳
            if (report.getCategoryId() != null) {
                switch (report.getCategoryId()) {
                    case 1: report.setCategoryName("転倒・転落"); break;
                    case 2: report.setCategoryName("誤嚥・窒息"); break;
                    case 3: report.setCategoryName("皮膚剥離"); break;
                    case 4: report.setCategoryName("誤薬、投薬漏れ"); break;
                    case 5: report.setCategoryName("食中毒・感染症等"); break;
                    case 6: report.setCategoryName("医療処置関連"); break;
                    case 7: report.setCategoryName("異食"); break;
                    case 8: report.setCategoryName("徘徊・行方不明"); break;
                    case 9: report.setCategoryName("職員の法令違反・不祥事"); break;
                    case 10: report.setCategoryName("不明"); break;
                    case 99: report.setCategoryName(report.getCategoryOther()); break; // その他の場合
                    default: report.setCategoryName("不明");
                }
            }

            // 発生場所IDを文字に翻訳
            if (report.getLocationId() != null) {
                switch (report.getLocationId()) {
                    case 1: report.setLocationName("居室"); break;
                    case 2: report.setLocationName("食堂等共用部"); break;
                    case 3: report.setLocationName("機能訓練室"); break;
                    case 4: report.setLocationName("浴室・脱衣"); break;
                    case 5: report.setLocationName("トイレ"); break;
                    case 6: report.setLocationName("廊下"); break;
                    case 7: report.setLocationName("敷地内の建物外"); break;
                    case 8: report.setLocationName("在宅"); break;
                    case 9: report.setLocationName("敷地外"); break;
                    case 99: report.setLocationName(report.getLocationOther()); break; // その他の場合
                    default: report.setLocationName("不明");
                }
            }
                if (report.getCreatedByUser() != null && report.getCreatedByUser().getStaffname() != null) {
                report.setBuilderName(report.getCreatedByUser().getStaffname());
                } else {
                report.setBuilderName("不明");
                }
        }

        // 3. 翻訳済みの5件のリストを "recentReports" という名前で画面に渡す
        model.addAttribute("recentReports", recentReports);
        
        return "home";
    }

    // ---------------------------------------------------------
    // （「送信」時の処理）
    // ---------------------------------------------------------
    @PostMapping("/submit-report")
    public String submitReport(
            @RequestParam("patient_id") Long patientId,
            @RequestParam("incident_date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime incidentDate,
            @RequestParam("location_id") Integer locationId,
            @RequestParam(value = "location_other", required = false) String locationOther,
            @RequestParam("category_id") Integer categoryId,
            @RequestParam(value = "category_other", required = false) String categoryOther,
            @RequestParam("medical_action_flag") Integer medicalActionFlag,
            @RequestParam(value = "medical_action", required = false) String medicalAction,
            @RequestParam("situation_desc") String situationDesc,
            @RequestParam("response_desc") String responseDesc,
            @RequestParam("patient-condition") String patientCondition) {

        CareReport report = new CareReport();
        report.setPatientId(patientId);
        report.setIncidentDate(incidentDate);
        report.setLocationId(locationId);
        report.setLocationOther(locationOther);
        report.setCategoryId(categoryId);
        report.setCategoryOther(categoryOther);
        report.setMedicalActionFlag(medicalActionFlag);
        report.setMedicalAction(medicalAction);
        report.setSituationDesc(situationDesc);
        report.setResponseDesc(responseDesc);
        report.setPatientCondition(patientCondition);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String loginId = userDetails.getUsername(); // ログインIDを取得
            
            // データベースから、そのログインIDを持つアカウント情報を探す
            UserAccount loggedInUser = userAccountRepository.findByLoginId(loginId).orElse(null);
            if (loggedInUser != null) {
                // 報告書の「作成者」としてセット
                report.setCreatedByUser(loggedInUser);
            }
        }

        careReportRepository.save(report);

        return "success";
    }
    // =========================================================
    // 報告書データを削除する処理
    // =========================================================
    @GetMapping("/delete_report")
    public String deleteReport(@RequestParam("id") Long id) {
        
        // 1. データベースから該当IDの報告書を削除する
        careReportRepository.deleteById(id);
        
        // 2. 削除が完了したら、報告書一覧画面にリダイレクトする
        return "redirect:/report_list";
    }

    // =========================================================
    // 報告書の詳細画面を表示する処理
    // =========================================================
    @GetMapping("/report_detail")
    public String showReportDetail(@RequestParam("id") Long id, Model model) {
        
        // 1. URLのIDを使って、データベースから該当する報告書を1件だけ取得
        CareReport report = careReportRepository.findById(id).orElse(null);

        if (report != null) {
            // 2. 患者IDから名前を取得してセットし、さらに患者データ自体も画面に渡す（介護度を表示するため）
            Long patientId = report.getPatientId();
            if (patientId != null) {
                patientRepository.findById(patientId).ifPresent(patient -> {
                    report.setPatientName(patient.getPatientName());
                    model.addAttribute("patient", patient); // 介護度などを取り出す用
                });
            }

            // 3. カテゴリーIDを文字に翻訳
            if (report.getCategoryId() != null) {
                switch (report.getCategoryId()) {
                    case 1: report.setCategoryName("転倒・転落"); break;
                    case 2: report.setCategoryName("誤嚥・窒息"); break;
                    case 3: report.setCategoryName("皮膚剥離"); break;
                    case 4: report.setCategoryName("誤薬、投薬漏れ"); break;
                    case 5: report.setCategoryName("食中毒・感染症等"); break;
                    case 6: report.setCategoryName("医療処置関連"); break;
                    case 7: report.setCategoryName("異食"); break;
                    case 8: report.setCategoryName("徘徊・行方不明"); break;
                    case 9: report.setCategoryName("職員の法令違反・不祥事"); break;
                    case 10: report.setCategoryName("不明"); break;
                    case 99: report.setCategoryName(report.getCategoryOther()); break; // その他の場合
                    default: report.setCategoryName("不明");
                }

            // 4. 発生場所IDを文字に翻訳
            switch (report.getLocationId()) {
                    case 1: report.setLocationName("居室"); break;
                    case 2: report.setLocationName("食堂等共用部"); break;
                    case 3: report.setLocationName("機能訓練室"); break;
                    case 4: report.setLocationName("浴室・脱衣"); break;
                    case 5: report.setLocationName("トイレ"); break;
                    case 6: report.setLocationName("廊下"); break;
                    case 7: report.setLocationName("敷地内の建物外"); break;
                    case 8: report.setLocationName("在宅"); break;
                    case 9: report.setLocationName("敷地外"); break;
                    case 99: report.setLocationName(report.getLocationOther()); break; // その他の場合
                    default: report.setLocationName("不明");
                }

                if (report.getCreatedByUser() != null && report.getCreatedByUser().getStaffname() != null) {
                report.setBuilderName(report.getCreatedByUser().getStaffname());
                } else {
                report.setBuilderName("不明");
                }
            }
        }

        // 5. 翻訳済みの報告書データを画面に渡す
        model.addAttribute("report", report);
        
        return "report_detail";
    }

    // =========================================================
    // ① 報告書の編集画面を表示する処理 (GET)
    // =========================================================
    @GetMapping("/edit_report")
    public String showEditReportForm(@RequestParam("id") Long id, Model model) {
        // 1. データベースから編集対象の報告書を1件取得
        CareReport report = careReportRepository.findById(id).orElse(null);
        
        // 2. プルダウンの選択肢用に、利用者全員のデータも取得
        List<Patient> patients = patientRepository.findAll();
        
        // 3. 画面にデータを渡す
        model.addAttribute("report", report);
        model.addAttribute("patients", patients);
        
        return "edit_report";
    }

    // =========================================================
    // ② 報告書の編集データを上書き保存する処理 (POST)
    // =========================================================
    @PostMapping("/update_report")
    public String updateReport(
            @RequestParam("id") Long id,
            @RequestParam("patient_id") Long patientId,
            // ▼ 400エラーの真犯人！ここで「この形の文字は日付だよ」と通訳します ▼
            @RequestParam("incident_date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime incidentDate,
            @RequestParam(value = "location_id", required = false) Integer locationId,
            @RequestParam(value = "location_other", required = false) String locationOther,
            @RequestParam(value = "category_id", required = false) Integer categoryId,
            @RequestParam(value = "category_other", required = false) String categoryOther,
            @RequestParam(value = "medical_action_flag", required = false) Integer medicalActionFlag,
            @RequestParam(value = "medical_action", required = false) String medicalAction,
            @RequestParam(value = "situation_desc", required = false) String situationDesc,
            @RequestParam(value = "response_desc", required = false) String responseDesc,
            // ▼ HTML側の name="patient-condition" を正しく受け取ります ▼
            @RequestParam(value = "patient-condition", required = false) String patientCondition) {


        CareReport report = careReportRepository.findById(id).orElse(null);

        if (report != null) {
            report.setPatientId(patientId);
            Patient patient = patientRepository.findById(patientId).orElse(null);
            if (patient != null) {
                report.setPatientName(patient.getPatientName());
            }

            report.setIncidentDate(incidentDate);
            report.setLocationId(locationId);
            report.setLocationOther(locationOther);
            report.setCategoryId(categoryId);
            report.setCategoryOther(categoryOther);
            report.setMedicalActionFlag(medicalActionFlag);
            report.setMedicalAction(medicalAction);
            report.setSituationDesc(situationDesc);
            report.setResponseDesc(responseDesc);
            report.setPatientCondition(patientCondition);

            // 3. 保存！（事後対応データは上書きされず、無事に生き残ります）
            careReportRepository.save(report);
        }

        return "redirect:/report_list";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/add_prevention")
    public String showPreventionForm(@RequestParam("id") Long id, Model model) {
        
        CareReport report = careReportRepository.findById(id).orElse(null);
        
        if (report != null) {
            Long patientId = report.getPatientId();
            if (patientId != null) {
                patientRepository.findById(patientId).ifPresent(patient -> {
                    report.setPatientName(patient.getPatientName());
                    // ▼ ここで確実に patient を画面に渡す！
                    model.addAttribute("patient", patient); 
                });
            }

            if (report.getCategoryId() != null) {
                String categoryName;
                switch (report.getCategoryId()) {
                    case 1: categoryName = "転倒・転落"; break;
                    case 2: categoryName = "誤嚥・窒息"; break;
                    case 3: categoryName = "皮膚剥離"; break;
                    case 4: categoryName = "誤薬、投薬漏れ"; break;
                    case 5: categoryName = "食中毒・感染症"; break;
                    case 6: categoryName = "医療処置関連"; break;
                    case 7: categoryName = "異食"; break;
                    case 8: categoryName = "徘徊・行方不明"; break;
                    case 9: categoryName = "職員の法令違反・不祥事"; break;
                    case 10: categoryName = ""; break;
                    case 99: 
                        categoryName = (report.getCategoryOther() != null && !report.getCategoryOther().isEmpty()) 
                                       ? "その他(" + report.getCategoryOther() + ")" : "その他"; 
                        break;
                    default: categoryName = "不明";
                }
                report.setCategoryName(categoryName);
            }

            if (report.getLocationId() != null) {
                String locationName;
                switch (report.getLocationId()) {
                    
                    case 99: 
                        locationName = (report.getLocationOther() != null && !report.getLocationOther().isEmpty()) 
                                       ? "その他(" + report.getLocationOther() + ")" : "その他"; 
                        break;
                    default: locationName = "不明";
                }
                report.setLocationName(locationName);
            }

            if (report.getLocationId() != null) {
                String locationName;
                switch (report.getLocationId()) {
                    case 1: report.setLocationName("居室"); break;
                    case 2: report.setLocationName("食堂等共用部"); break;
                    case 3: report.setLocationName("機能訓練室"); break;
                    case 4: report.setLocationName("浴室・脱衣"); break;
                    case 5: report.setLocationName("トイレ"); break;
                    case 6: report.setLocationName("廊下"); break;
                    case 7: report.setLocationName("敷地内の建物外"); break;
                    case 8: report.setLocationName("在宅"); break;
                    case 9: report.setLocationName("敷地外"); break;
                    case 99: 
                        locationName = (report.getLocationOther() != null && !report.getLocationOther().isEmpty()) 
                                       ? "その他(" + report.getLocationOther() + ")" : "その他"; 
                        break;
                    default: locationName = "不明";
                }
 
            }

            if (report.getCreatedByUser() != null && report.getCreatedByUser().getStaffname() != null) {
                report.setBuilderName(report.getCreatedByUser().getStaffname());
                } else {
                report.setBuilderName("不明");
                }

        }
        model.addAttribute("report", report);

        return "add_prevention";
    }





    @GetMapping("/prevention_list")
    public String showPreventionList(Model model) {
        
        List<CareReport> allReports = careReportRepository.findAll();


        for (CareReport report : allReports) {
            
            // 【利用者名】のセット
            Long patientId = report.getPatientId();
            if (patientId != null) {
                patientRepository.findById(patientId).ifPresent(patient -> {
                    report.setPatientName(patient.getPatientName());
                });
            }

            // 【カテゴリー名】のセット（直接セットする形に修正）
            if (report.getCategoryId() != null) {
                switch (report.getCategoryId()) {
                    case 1: report.setCategoryName("転倒・転落"); break;
                    case 2: report.setCategoryName("誤嚥・窒息"); break;
                    case 3: report.setCategoryName("皮膚剥離"); break;
                    case 4: report.setCategoryName("誤薬、投薬漏れ"); break;
                    case 5: report.setCategoryName("食中毒・感染症等"); break;
                    case 6: report.setCategoryName("医療処置関連"); break;
                    case 7: report.setCategoryName("異食"); break;
                    case 8: report.setCategoryName("徘徊・行方不明"); break;
                    case 9: report.setCategoryName("職員の法令違反・不祥事"); break;
                    case 10: report.setCategoryName("不明"); break;
                    case 99: report.setCategoryName(report.getCategoryOther() != null ? report.getCategoryOther() : "その他"); break;
                    default: report.setCategoryName("不明");
                }
            }

            // ▼▼ まるごと抜けていた【発生場所名】のセットを追加 ▼▼
            if (report.getLocationId() != null) {
                switch (report.getLocationId()) {
                    case 1: report.setLocationName("居室"); break;
                    case 2: report.setLocationName("食堂等共用部"); break;
                    case 3: report.setLocationName("機能訓練室"); break;
                    case 4: report.setLocationName("浴室・脱衣"); break;
                    case 5: report.setLocationName("トイレ"); break;
                    case 6: report.setLocationName("廊下"); break;
                    case 7: report.setLocationName("敷地内の建物外"); break;
                    case 8: report.setLocationName("在宅"); break;
                    case 9: report.setLocationName("敷地外"); break;
                    case 99: report.setLocationName(report.getLocationOther() != null ? report.getLocationOther() : "その他"); break;
                    default: report.setLocationName("不明");
                }
            }

            // 【発見者（作成者）名】のセット
            if (report.getCreatedByUser() != null && report.getCreatedByUser().getStaffname() != null) {
                report.setBuilderName(report.getCreatedByUser().getStaffname());
            } else {
                report.setBuilderName("不明");
            }
        }
        // 未完了（新しい順）
        List<CareReport> reports = allReports.stream()
                .filter(r -> r.getCompleteCheck() == null || !r.getCompleteCheck())
                .sorted(Comparator.comparing(r -> r.getIncidentDate(),Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());



        model.addAttribute("reports", reports);
        
        return "prevention_list";
    }


    @PostMapping("/update_prevention")
    public String updatePrevention(
            @RequestParam("id") Long id,
            @RequestParam(value = "family_report", required = false) String familyReport,
            @RequestParam(value = "cause_analysis", required = false) String causeAnalysis,
            @RequestParam(value = "preventive_measure", required = false) String preventiveMeasure,
            @RequestParam(value = "manager_check", required = false) Boolean managerCheck,
            @RequestParam(value = "complete_check", required = false) Boolean completeCheck) {

        CareReport report = careReportRepository.findById(id).orElse(null);

        if (report != null) {
            report.setFamilyReport(familyReport);
            report.setCauseAnalysis(causeAnalysis);
            report.setPreventiveMeasure(preventiveMeasure);
            report.setManagerCheck(managerCheck != null ? managerCheck : false);
            report.setCompleteCheck(completeCheck != null ? completeCheck : false);
            
            careReportRepository.save(report);
        }

        // 保存後は対象リスト画面に戻る
        return "redirect:/prevention_list";
    }

}