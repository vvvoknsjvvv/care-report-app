
-- 1. 利用者（Patient）の仮データ作成
INSERT INTO patients (patient_name) VALUES ('山田 太郎');
INSERT INTO patients (patient_name) VALUES ('鈴木 トメ');
INSERT INTO patients (patient_name) VALUES ('佐藤 一郎');


-- 2. 事故報告書（CareReport）の仮データ作成
INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (1, '2026-08-01 10:30:00', 1, 1, 'ベッドから立ち上がろうとしてバランスを崩し尻餅をついた。', 'すぐに駆け寄りバイタル測定。外傷なし。', '痛みの訴えなし。歩行状態も普段と変わりなし。');

INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (2, '2026-08-15 12:15:00', 2, 2, '昼食時にむせ込みがあり、顔色が一時的に悪くなった。', '背部タッピングを実施し、水分摂取を促した。', '呼吸状態落ち着き、顔色も回復。食事は中止した。');

INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (3, '2026-08-20 02:00:00', 4, 1, '夜間トイレ誘導時、ふらつきがあり壁に手をついた。', '付き添いを強化し、車椅子での移動に変更。', '怪我なし。夜間せん妄の影響か少しぼーっとしている。');


INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (1, '2026-08-05 14:00:00', 3, 3, '入浴時の移乗介助中、シャワーキャリーのひじ掛けに右腕が触れ、前腕部に2cm程度の表皮剥離が発生。', '直ちにシャワーで洗浄し、看護師へ報告。ワセリンと保護パッドで処置を実施。', '出血はすぐに止まり、本人は痛みを訴えていない。');
INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (2, '2026-08-08 08:30:00', 1, 4, '朝食後、居室の床に朝の定期薬（降圧剤1錠）が落ちているのを清掃中のスタッフが発見した。', '看護師に報告し、落ちていた薬は破棄。バイタルを確認し、主治医の指示を仰いだ。', '血圧は平常通り(135/82)で、体調に変化なし。');
INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (3, '2026-08-10 16:45:00', 5, 1, '歩行器での移動中、廊下でスリッパが脱げそうになりバランスを崩して転倒。', '意識確認、バイタル測定を実施。頭部打撲がないか全身をチェックし、複数名でベッドへ移乗。', '右膝に軽度の発赤あり。冷罨法を実施し、歩行時の痛みが引くまで車椅子対応とした。');
INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (1, '2026-08-12 12:30:00', 2, 1, '昼食中、車椅子に浅く座っていたため徐々にお尻が前に滑り、床にずり落ちるように転落した。', 'スタッフで抱え上げてベッドへ誘導。姿勢が崩れやすい原因を確認するため、車椅子のクッションとフットレストを見直した。', '目立った外傷なし。食事はベッド上でギャッチアップして再開した。');
INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (2, '2026-08-14 06:00:00', 1, 3, '起床時の更衣介助の際、左すね付近に乾燥による掻きむしり痕（軽度の出血・皮膚剥離）を発見。', '看護師に報告。微温湯で清拭後、処方されている保湿剤とステロイド軟膏を塗布しガーゼ保護。', 'かゆみが強い様子だったが、処置後は落ち着かれている。');
INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (3, '2026-08-16 10:15:00', 6, 1, '中庭を散歩中、敷地内の段差につまずき膝から崩れ落ちるように転倒。', '直ちに車椅子を手配し、居室へ戻りバイタル測定と全身観察。', '両膝に擦過傷あり。看護師により消毒と絆創膏処置を実施。歩行状態に異常なし。');
INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (1, '2026-08-18 15:30:00', 1, 5, 'おむつ交換時、右大腿部外側に5cm×3cmの青あざ（内出血）があるのを発見。発生原因は不明。', '前日の入浴時記録を確認したが記載なし。スタッフ間で情報共有し、移乗時の接触がないか注意喚起。', '押さえると軽い痛みがある様子。熱感や腫れはないため経過観察とする。');
INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (2, '2026-08-21 19:00:00', 3, 4, '入浴後、背部に塗布指示のある処方軟膏を塗り忘れたまま就寝準備を終えてしまった。', '遅番スタッフが記録チェック時に気づき、訪室して塗布を実施した。', '塗布の遅れによる皮膚状態の悪化や本人の不快感はなし。');
INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (3, '2026-08-22 15:10:00', 2, 2, 'おやつ時、とろみ無しの麦茶を一口飲んだ直後に激しくむせ込み、顔色が一時的に赤くなった。', 'すぐに背部タッピングを行い、咳き込みが落ち着くまで寄り添い見守った。', '5分ほどで呼吸状態は安定し、サチュレーションも98%に回復。念のため水分には薄いとろみをつけるよう変更。');
INSERT INTO care_reports (patient_id, incident_date, location_id, category_id, situation_desc, response_desc, patient_condition) 
VALUES (1, '2026-08-23 09:00:00', 1, 99, '洗面台で顔を洗う際、外して置いていた眼鏡を誤って手で払い落としてしまい、フレームが変形しレンズが外れた。', '割れたレンズの破片がないか周辺を清掃。ご家族へ連絡し、予備の眼鏡を持参いただくよう依頼した。', '本人は怪我なし。「見えなくて不便だ」と少し落ち着かないご様子。');