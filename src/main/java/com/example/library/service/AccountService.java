package com.example.library.service;

import com.example.library.exceptions.AlreadyExistException;
import com.example.library.exceptions.PasswordsNotMatchingException;
import com.example.library.model.User;
import com.example.library.model.UserDTO;
import com.example.library.model.enums.UserRole;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    UserRepository userRepository;

    @Autowired
    public AccountService(UserRepository userRepository){
        this.userRepository =  userRepository;
    }
    public void saveUser(UserDTO user) throws PasswordsNotMatchingException, AlreadyExistException {
        if (userRepository.existsByEmail(user.getEmail()))
            throw new AlreadyExistException();
        if (!user.getPassword().equals(user.getMatchingPassword()))
            throw new PasswordsNotMatchingException();
        userRepository.save(new User(UserRole.CUSTOMER,user.getName(), user.getSurname(), user.getEmail(), user.getPassword()));
    }
}
