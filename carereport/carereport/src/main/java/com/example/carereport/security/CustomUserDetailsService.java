package com.example.carereport.security;

import com.example.carereport.entity.UserAccount;
import com.example.carereport.repository.UserAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public CustomUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        

        UserAccount userAccount = userAccountRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));

        return new User(
                userAccount.getLoginId(),
                userAccount.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(userAccount.getRole()))
        );
    }
}