package com.linhdev.identityservice.validation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// 2 param:
// - Annotation Validator chịu trách nhiệm
// - Kiểu dữ liệu data muốn validate
public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;

    // Get data trong lớp cần valid trước khi chuyển vào isValid
    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) return true;

        long years = ChronoUnit.YEARS.between(value, LocalDate.now());

        return years >= min;
    }
}
