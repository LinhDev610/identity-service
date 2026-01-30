package com.linhdev.identityservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Target({FIELD})    // Annotation apply ở đâu
@Retention(RUNTIME) // Annotation này xử lý lúc nào
// Bản thân annotation này mới chỉ có tác dụng khai báo, cần tạo thêm lớp validateBy để xử lý data
@Constraint(validatedBy = { DobValidator.class })
public @interface DobConstraint {
    String message() default "Invalid date of birth";

    int min();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
