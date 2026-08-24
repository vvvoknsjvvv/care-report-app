package com.example.carereport.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ログインユーザー（アカウント）テーブルの設計図
 */
@Entity
@Table(name = "user_accounts") 
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ログインID（unique = true で重複登録を防ぎます）
    @Column(unique = true, nullable = false)
    private String loginId;

    // パスワード（あとで暗号化された複雑な文字列が入ります）
    @Column(nullable = false)
    private String password;

    // 権限（"ROLE_ADMIN" または "ROLE_USER" が入ります）
    @Column(nullable = false)
    private String role;

    // =========================================
    // Getter と Setter
    // =========================================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}