package com.example.ApiArenaXperience.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PrecioValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrecio {

    String message() default "El precio debe estar entre 10 y 20.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
