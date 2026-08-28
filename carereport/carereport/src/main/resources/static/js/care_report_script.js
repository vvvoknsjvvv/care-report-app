// 医療的対応のラジオボタン切り替えでテキストエリアの表示/非表示を制御
function toggleMedicalText() {
  const flag = document.querySelector(
    'input[name="medical_action_flag"]:checked',
  ).value;
  const textArea = document.getElementById("medical_action_text");

  if (flag === "1") {
    textArea.style.display = "block";
  } else {
    textArea.style.display = "none";
    textArea.value = ""; // 中身をクリア
  }
}

// 発生場所の「その他」切り替えで入力フォームの表示/非表示を制御
function toggleLocationOther() {
  const checkedRadio = document.querySelector(
    'input[name="location_id"]:checked',
  );
  if (!checkedRadio) return;

  const value = checkedRadio.value;
  const otherInput = document.getElementById("location_other_text");

  // 発生場所の「その他」は value="99"
  if (value === "99") {
    otherInput.style.display = "block";
  } else {
    otherInput.style.display = "none";
    otherInput.value = ""; // 中身をクリア
  }
}

// 事故カテゴリーの「その他」切り替えで入力フォームの表示/非表示を制御
function toggleCategoryOther() {
  const checkedRadio = document.querySelector(
    'input[name="category_id"]:checked',
  );
  if (!checkedRadio) return;

  const value = checkedRadio.value;
  const otherInput = document.getElementById("category_other_text");

  // HTMLに合わせて条件を value="6" に修正
  if (value === "99") {
    otherInput.style.display = "block";
  } else {
    otherInput.style.display = "none";
    otherInput.value = ""; // 中身をクリア
  }
}

// 初期化とイベントリスナーの登録
document.addEventListener("DOMContentLoaded", function () {
  const reportForm = document.getElementById("reportForm");

  if (reportForm) {
    // フォーム送信前のフロントエンドバリデーション
    reportForm.addEventListener("submit", function (e) {
      const patientId = document.getElementById("patient_id").value;
      if (!patientId) {
        e.preventDefault(); // 送信をストップ
        document.getElementById("patient-error").style.display = "block";
      } else {
        document.getElementById("patient-error").style.display = "none";
      }
    });

    // --- ラジオボタンが変更された時のイベントリスナー ---
    // 発生場所のラジオボタンを監視
    const locationRadios = reportForm.querySelectorAll(
      'input[name="location_id"]',
    );
    locationRadios.forEach((radio) => {
      radio.addEventListener("change", toggleLocationOther);
    });

    // 事故カテゴリーのラジオボタンを監視
    const categoryRadios = reportForm.querySelectorAll(
      'input[name="category_id"]',
    );
    categoryRadios.forEach((radio) => {
      radio.addEventListener("change", toggleCategoryOther);
    });
  }
});

// 発生日時の初期値を現在時刻にセットする
window.onload = function () {
  const incidentDateInput = document.getElementById("incident_date");
  if (incidentDateInput) {
    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    incidentDateInput.value = now.toISOString().slice(0, 16);
  }
};

// ==========================================
// 入力中のデータ消失防止（ページ離脱アラート）
// ==========================================

let isFormDirty = false; // 入力内容が変更されたかどうかのフラグ

document.addEventListener("DOMContentLoaded", function () {
  const formElements = document.querySelectorAll(
    "form input, form select, form textarea",
  );
  formElements.forEach((element) => {
    element.addEventListener("change", () => {
      isFormDirty = true;
    });
    element.addEventListener("input", () => {
      isFormDirty = true;
    });
  });
  const allForms = document.querySelectorAll("form");
  allForms.forEach((form) => {
    form.addEventListener("submit", () => {
      isFormDirty = false;
    });
  });

  window.addEventListener("beforeunload", function (e) {
    if (isFormDirty) {
      e.preventDefault();
      e.returnValue = "";
    }
  });
});

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("preventionForm");

  if (form) {
    form.addEventListener("submit", function (event) {
      // 「報告書作成を完了する」チェックボックスの状態を取得
      const completeCheck = document.getElementById("complete_check");

      if (completeCheck && completeCheck.checked) {
        // チェックが入っている場合はポップアップを表示
        const confirmed = confirm(
          "すべての作業を終えて報告書作成を完了しますか？\n※完了状態になります。",
        );

        if (!confirmed) {
          // 「キャンセル」が押されたら、送信をストップする
          event.preventDefault();
        }
      }
    });
  }
});
