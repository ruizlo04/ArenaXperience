package com.example.ApiArenaXperience.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PrecioValidator implements ConstraintValidator<ValidPrecio, Double> {

    @Override
    public boolean isValid(Double precio, ConstraintValidatorContext context) {
        if (precio == null) {
            return false;
        }
        return precio >= 10 && precio <= 20;
    }
}
