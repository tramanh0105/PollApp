package com.pollapp.pollapp.security;

import com.pollapp.pollapp.exception.UserIdNotFoundException;
import com.pollapp.pollapp.user.User;
import com.pollapp.pollapp.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.findByUsername(username);
             if(user == null){
                 throw new UsernameNotFoundException("Username not found");
             }
        return UserPrincipal.createUserPrincipalFromUser(user);
    }
    public UserDetails loadUserByUserId (Long id) throws UserIdNotFoundException {
        User user = this.userRepo.findById(id).get();
        if(user == null){
            throw new UserIdNotFoundException("Userid not found");

        }
        return UserPrincipal.createUserPrincipalFromUser(user);
    }
}
