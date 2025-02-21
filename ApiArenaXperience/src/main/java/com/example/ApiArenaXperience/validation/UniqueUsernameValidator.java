package com.example.ApiArenaXperience.validation;

import com.example.ApiArenaXperience.repo.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String>{

        @Autowired
        private UserRepository repo;

        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
            return StringUtils.hasText(s) && !repo.existsByUsername(s);
        }
}
