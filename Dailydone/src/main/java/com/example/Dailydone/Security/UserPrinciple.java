package com.example.Dailydone.Security;

import com.example.Dailydone.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrinciple implements UserDetails {
    private final User userprofile;
    public UserPrinciple(User user){
        this.userprofile = user;
    }
    public User GetUser(){
        return userprofile;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return userprofile.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return userprofile.getEmail();
    }
}
