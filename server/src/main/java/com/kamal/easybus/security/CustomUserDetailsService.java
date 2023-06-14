package com.kamal.easybus.security;

import com.kamal.easybus.exceptions.ResourceNotFoundException;
import com.kamal.easybus.entities.Admin;
import com.kamal.easybus.repos.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class CustomUserDetailsService implements UserDetailsService {

    private AdminRepo adminRepository;

    public CustomUserDetailsService(AdminRepo adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

        return UserPrincipal.create(admin);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(admin);
    }
}