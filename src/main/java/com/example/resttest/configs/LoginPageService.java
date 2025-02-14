package com.example.resttest.configs;

import com.example.resttest.Models.User;
import com.example.resttest.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginPageService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);

        return UserDetailsImpl.build(user);
//                user.getName(),
//                user.getPassword(),
//                true, true, true, true,
//                AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
    }
}
