-- ==========================================
-- 1. 利用者（patients）のテストデータ
-- ==========================================
INSERT INTO patients (patient_name, birth_date, care_level, medical_history, emergency_contact) 
VALUES ('山田 太郎', '1945-05-12', '要介護3', '高血圧、糖尿病', '長男（山田 一郎） 090-1111-2222');

INSERT INTO patients (patient_name, birth_date, care_level, medical_history, emergency_contact) 
VALUES ('鈴木 花子', '1950-10-25', '要介護1', '骨粗鬆症', '長女（佐藤 梅子） 080-3333-4444');

INSERT INTO patients (patient_name, birth_date, care_level, medical_history, emergency_contact) 
VALUES ('田中 一郎', '1938-03-03', '要支援2', '緑内障', NULL);

-- ==========================================
-- 2. 報告書（care_reports）のテストデータ
-- ==========================================

-- ① 未完了の新しい事故（事後対応は未入力）
INSERT INTO care_reports (
    patient_id, incident_date, location_id, category_id, 
    medical_action_flag, medical_action, situation_desc, response_desc, patient_condition,
    manager_check, complete_check
) VALUES (
    1, '2026-08-28 09:30:00', 1, 1, 
    1, '湿布貼付', 'ベッドから立ち上がろうとしてバランスを崩し転倒。', 'バイタル測定、全身状態観察、湿布貼付。', '痛みはあるが意識クリア。',
    false, false
);

-- ② 施設長報告済みだが、まだ未完了の事故
INSERT INTO care_reports (
    patient_id, incident_date, location_id, category_id, 
    medical_action_flag, medical_action, situation_desc, response_desc, patient_condition,
    family_report, cause_analysis, preventive_measure,
    manager_check, complete_check
) VALUES (
    2, '2026-08-27 15:00:00', 2, 2, 
    0, NULL, 'おやつ（お餅）を急いで食べてむせた。', '背部叩打、水分提供。', 'その後は落ち着いて完食。',
    '長女へ電話にて報告済。', '急いで食べてしまったこと。', '食事中は職員が近くで見守る。',
    true, false
);

-- ③ すべて完了済みの過去の事故（一覧では非表示、管理者のみ詳細から編集可能）
INSERT INTO care_reports (
    patient_id, incident_date, location_id, category_id, 
    medical_action_flag, medical_action, situation_desc, response_desc, patient_condition,
    family_report, cause_analysis, preventive_measure,
    manager_check, complete_check
) VALUES (
    3, '2026-08-20 10:00:00', 5, 3, 
    1, 'ワセリン塗布', '入浴時に背中を擦りすぎて皮膚剥離。', '看護師による処置。', '特変なし。',
    '家族へ報告済み。', '職員の介助時の力加減が強かった。', '介助手順の見直し、職員研修の実施。',
    true, true
);