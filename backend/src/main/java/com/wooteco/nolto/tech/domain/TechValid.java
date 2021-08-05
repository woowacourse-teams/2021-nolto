package com.wooteco.nolto.tech.domain;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = TechValidator.class)
public @interface TechValid {

    String message() default "기술 스택이 중복됩니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
