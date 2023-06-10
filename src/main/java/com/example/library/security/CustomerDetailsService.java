package com.example.library.security;

import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomerDetailsService implements UserDetailsService {
        private final UserRepository userRepository;

        @Autowired
        public CustomerDetailsService(UserRepository userRepository){
            this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
            final Optional<com.example.library.model.User> optionalUser = userRepository.findByEmail(email);
            if (!optionalUser.isPresent())
                throw new UsernameNotFoundException("Check your password and email and try again");
            com.example.library.model.User user = optionalUser.get();
            return User.withUsername(user.getEmail()).password(user.getPassword()).roles(user.getUserRole().toString()).build();
        }
}
