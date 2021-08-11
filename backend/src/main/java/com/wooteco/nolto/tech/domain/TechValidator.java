package com.wooteco.nolto.tech.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;

public class TechValidator implements ConstraintValidator<TechValid, List<Long>> {

    @Override
    public void initialize(TechValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<Long> techs, ConstraintValidatorContext context) {
        HashSet<Long> techSet = new HashSet<>(techs);
        return techs.size() == techSet.size();
    }
}
