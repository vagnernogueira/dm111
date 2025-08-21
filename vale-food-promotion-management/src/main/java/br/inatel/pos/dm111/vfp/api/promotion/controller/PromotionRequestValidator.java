package br.inatel.pos.dm111.vfp.api.promotion.controller;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import br.inatel.pos.dm111.vfp.api.promotion.PromotionRequest;

@Component
public class PromotionRequestValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return PromotionRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", "userId.empty", "userId is required!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "restaurantId", "restaurantId.empty", "restaurantId is required!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "productId", "productId.empty", "User Id is required!");
		ValidationUtils.rejectIfEmpty(errors, "price", "price.empty", "price are required!");
	}
}
