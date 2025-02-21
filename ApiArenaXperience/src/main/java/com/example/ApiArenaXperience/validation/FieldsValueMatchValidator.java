package com.example.ApiArenaXperience.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.java.Log;
import org.springframework.beans.PropertyAccessorFactory;


@Log
public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object fieldValue = PropertyAccessorFactory
                .forBeanPropertyAccess(o).getPropertyValue(field);
        Object fieldValueMatch = PropertyAccessorFactory
                .forBeanPropertyAccess(o).getPropertyValue(fieldMatch);

        if (fieldValue != null)
            return fieldValue.equals(fieldValueMatch);
        else
            return fieldValueMatch == null;

    }
}
