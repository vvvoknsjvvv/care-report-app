package com.example.carereport.controller;

import com.example.carereport.entity.Patient;
import com.example.carereport.repository.PatientRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 利用者に関する画面遷移やデータ処理の受付窓口（Controller）
 */
@Controller
public class PatientController {

    // データベース操作の窓口（リポジトリ）を呼び出す準備
    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

@GetMapping("/patient_list")
    public String showPatientList(@RequestParam(defaultValue = "0") int page, Model model) {
        
        // 1. 「1ページあたり10件、IDの降順（新しい登録順）」という条件を作る
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        
        // 2. その条件でデータベースから取得（List ではなく Page という箱に入ります）
        Page<Patient> patientPage = patientRepository.findAll(pageable);
        
        model.addAttribute("patientPage", patientPage);
        
        return "patient_list";
    }

    @GetMapping("/add_patient")
    public String showAddPatientForm() {
        // templates/add_patient.html を表示する
        return "add_patient";
    }
    // HTMLのフォームから「POST」で送信されたデータを受け取る設定
    @PostMapping("/register-patient")
    public String registerPatient(
            @RequestParam("patient_name") String patientName,
            @RequestParam("birth_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
            @RequestParam("care_level") String careLevel,
            @RequestParam("medical_history") String medicalHistory) {

        // 1. 空の利用者データ（設計図）を用意する
        Patient patient = new Patient();

        // 2. HTMLの入力フォームから届いたデータをセットする
        patient.setPatientName(patientName);
        patient.setBirthDate(birthDate);
        patient.setCareLevel(careLevel);
        patient.setMedicalHistory(medicalHistory);

        // 3. データベースに保存する（SQLを書かずにこれ1行でINSERT完了！）
        patientRepository.save(patient);

        // 4. 処理が終わったら、完了画面（success.html）へ強制移動（リダイレクト）させる
        return "success";
    }

    // =========================================================
    // 利用者データを削除する処理
    // =========================================================
    @GetMapping("/delete_patient")
    public String deletePatient(@RequestParam("id") Long id) {
        
        // 1. データベースから該当IDの利用者を削除する
        patientRepository.deleteById(id);
        
        // 2. 削除が完了したら、もう一度利用者一覧画面に強制移動（リダイレクト）する
        return "redirect:/patient_list";
    }
    // =========================================================
    // 編集画面を表示 (GET)
    // =========================================================
    @GetMapping("/edit_patient")
    public String showEditPatientForm(@RequestParam("id") Long id, Model model) {
        Patient patient = patientRepository.findById(id).orElse(null);
        model.addAttribute("patient", patient);
        return "edit_patient";
    }

    // =========================================================
    // 編集データを上書き保存する処理 (POST)
    // =========================================================
    @PostMapping("/update_patient")
    public String updatePatient(
            @RequestParam("id") Long id, 
            @RequestParam("patient_name") String patientName,
            @RequestParam("birth_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
            @RequestParam("care_level") String careLevel,
            @RequestParam("medical_history") String medicalHistory) {

        Patient patient = patientRepository.findById(id).orElse(new Patient());
        patient.setPatientName(patientName);
        patient.setBirthDate(birthDate);
        patient.setCareLevel(careLevel);
        patient.setMedicalHistory(medicalHistory);
        patientRepository.save(patient);
        return "redirect:/patient_list";
    }
}