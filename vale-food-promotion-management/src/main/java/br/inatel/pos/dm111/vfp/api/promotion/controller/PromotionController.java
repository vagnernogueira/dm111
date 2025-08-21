package br.inatel.pos.dm111.vfp.api.promotion.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.inatel.pos.dm111.vfp.api.core.ApiException;
import br.inatel.pos.dm111.vfp.api.core.AppError;
import br.inatel.pos.dm111.vfp.api.promotion.PromotionRequest;
import br.inatel.pos.dm111.vfp.api.promotion.PromotionResponse;
import br.inatel.pos.dm111.vfp.api.promotion.service.PromotionService;

@RestController
@RequestMapping("/valefood/promotions")
public class PromotionController {
	private static final Logger log = LoggerFactory.getLogger(PromotionController.class);
	private final PromotionService service;
	private final PromotionRequestValidator validator;

	public PromotionController(PromotionService service, PromotionRequestValidator validator) {
		this.service = service;
		this.validator = validator;
	}

	@GetMapping
	public ResponseEntity<List<PromotionResponse>> getAllPromotions() throws ApiException {
		log.debug("Received request to list all promotions.");
		var response = service.searchPromotions();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/{userId}")
	public ResponseEntity<List<PromotionResponse>> getAllPromotions(@PathVariable("userId") String userId)
			throws ApiException {
		log.debug("Received request to list all promotions by user.");
		var response = service.searchPromotionsByUserPreferredCategories(userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<PromotionResponse> postPromotion(@RequestBody PromotionRequest request,
			BindingResult bindingResult) throws ApiException {
		log.debug("Received request to create a new promotion...");
		validateRequest(request, bindingResult);
		var response = service.createPromotion(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping(value = "/{userId}/{restaurantId}")
	public ResponseEntity<List<PromotionResponse>> getPromotionsByRestaurant(@PathVariable("userId") String userId,
			@PathVariable("restaurantId") String restaurantId) throws ApiException {
		log.debug("Received request to list promotions by restaurant.");
		var response = service.searchPromotions(userId, restaurantId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/{userId}/{promotionId}")
	public ResponseEntity<PromotionResponse> getPromotion(@PathVariable("userId") String userId,
			@PathVariable("promotionId") String promotionId) throws ApiException {
		log.debug("Received request to list promotions by restaurant.");
		var response = service.searchPromotion(userId, promotionId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping(value = "/{promotionId}")
	public ResponseEntity<PromotionResponse> putPromotion(@RequestBody PromotionRequest request,
			@PathVariable("promotionId") String promotionId, BindingResult bindingResult) throws ApiException {
		log.debug("Received request to update a promotion...");
		validateRequest(request, bindingResult);
		var response = service.updatePromotion(request, promotionId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping(value = "/{userId}/{promotionId}")
	public ResponseEntity<List<PromotionResponse>> deleteRestaurant(@PathVariable("userId") String userId,
			@PathVariable("promotionId") String promotionId) throws ApiException {
		log.debug("Received request to delete an restaurant: id {}", promotionId);
		service.removePromotion(promotionId, userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	private void validateRequest(PromotionRequest request, BindingResult bindingResult) throws ApiException {
		ValidationUtils.invokeValidator(validator, request, bindingResult);
		if (bindingResult.hasErrors()) {
			var errors = bindingResult.getFieldErrors().stream()
					.map(fe -> new AppError(fe.getCode(), fe.getDefaultMessage())).toList();
			throw new ApiException(HttpStatus.BAD_REQUEST, errors);
		}
	}
}
