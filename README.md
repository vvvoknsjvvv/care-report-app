## 〇概要

- 介護現場での事故・ヒヤリハット報告の管理をweb上で一元管理するシステム。
- スマートフォンでの使用を想定し、スタッフが直感的に操作できるようにUIを調整して作成。

## 〇制作背景

前職の職場では紙での管理がされ過去の報告書を活用する機会がなかったため、会議などでは直近2か月程度の報告書しか使われなかった。報告書一覧でソート機能を実装することで、過去の事例も参照して再発防止策を検討できるように。
※重大性の高い介護事故は行政のフォーマット・書式での提出が必要なため、このシステムでは対象外。
承認機能もケアマネージャーとの再発防止策の作成があるため、口頭・対面での確認を想定して未実装。

## 〇仕様技術

- Java (springBoot)
- Spring Security, Spring Data JPA
- H2 Database
- HTML, CSS, Thymeleaf, Bootstrap

## 〇起動方法

**Java 11以上のインストールが必須**
フォルダ内の 「起動.bat」をダブルクリック
停止時は ctrl+cで「バッチ ジョブを終了しますか (Y/N)?」が表示されるので、yで停止。

## 〇ログイン画面

起動後、http://localhost:8081/login からアクセス。

## 〇H2データベース確認ページ

JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: （空欄）でログイン
http://localhost:8081/h2-console

## 〇ポート番号を変更するファイル

src/main/resources/application.propertiesの server.port=\*\*\*\* を変更。
**変更時はMavenで再度パッケージ化**

## 〇自動作成初期化アカウント

- 管理者アカウント
  id: admin
  pass: admin123

- 一般アカウント
  id: staff
  pass: staff123

## 〇挙動確認用の仮データ操作

src/main/resources/にあるdata.sqlで仮データ生成。
不要な場合は削除。
**変更時はMavenで再度パッケージ化**
