package br.inatel.pos.dm111.vfp.api.promotion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.inatel.pos.dm111.vfp.api.core.ApiException;
import br.inatel.pos.dm111.vfp.api.core.AppErrorCode;
import br.inatel.pos.dm111.vfp.api.promotion.PromotionRequest;
import br.inatel.pos.dm111.vfp.api.promotion.PromotionResponse;
import br.inatel.pos.dm111.vfp.persistence.promotion.Promotion;
import br.inatel.pos.dm111.vfp.persistence.promotion.PromotionRepository;
import br.inatel.pos.dm111.vfp.persistence.restaurant.Restaurant;
import br.inatel.pos.dm111.vfp.persistence.restaurant.RestaurantRepository;
import br.inatel.pos.dm111.vfp.persistence.user.User;
import br.inatel.pos.dm111.vfp.persistence.user.UserRepository;
import br.inatel.pos.dm111.vfp.publisher.AppPublisher;

@Service
public class PromotionService {

	private static final Logger log = LoggerFactory.getLogger(PromotionService.class);

	private final PromotionRepository promotionRepository;

	private final RestaurantRepository restaurantRepository;

	private final UserRepository userRepository;

	private final AppPublisher promotionPublisher;

	public PromotionService(PromotionRepository promotionRepository, RestaurantRepository restaurantRepository,
			UserRepository userRepository, AppPublisher promotionPublisher) {
		this.promotionRepository = promotionRepository;
		this.restaurantRepository = restaurantRepository;
		this.userRepository = userRepository;
		this.promotionPublisher = promotionPublisher;
	}

	/**
	 * User context
	 * ---------------------------------------------------------------------------------------------
	 */
	public List<PromotionResponse> searchPromotions() throws ApiException {
		return retrievePromotions().stream().map(this::buildPromotionResponse).toList();
	}

	public List<PromotionResponse> searchPromotionsByUserPreferredCategories(String userId) throws ApiException {
		var userOpt = retrieveUserById(userId);
		if (userOpt.isEmpty()) {
			log.warn("User was not found. Id: {}", userId);
			throw new ApiException(AppErrorCode.USER_NOT_FOUND);
		}
		List<Promotion> promotions = new ArrayList<>();
		userOpt.get().preferredCategories().forEach(category -> {
			try {
				promotions.addAll(retrievePromotions().stream()
						.filter(promotion -> promotion.category().equals(category)).toList());
			} catch (ApiException e) {
				e.printStackTrace();
			}
		});
		return promotions.stream().map(this::buildPromotionResponse).toList();
	}

	/**
	 * Restaurant context
	 * ---------------------------------------------------------------------------------------------
	 */

	public PromotionResponse createPromotion(PromotionRequest request) throws ApiException {
		validatePromotion(request);
		var promotion = buildPromotion(request);
		promotionRepository.save(promotion);
		var published = promotionPublisher.publishCreated(promotion);
		if (!published) {
			log.error("Promotion created was not published. Needs to be re published later on... Promotion Id: {}",
					promotion.id());
		}
		log.info("Promotion was successfully created. Id: {}", promotion.id());
		return buildPromotionResponse(promotion);
	}

	public List<PromotionResponse> searchPromotions(String userId, String restaurantId) throws ApiException {
		validateUser(userId);
		return retrievePromotions().stream().filter(promotion -> promotion.restaurantId().equals(restaurantId))
				.map(this::buildPromotionResponse).toList();
	}

	public PromotionResponse searchPromotion(String userId, String promotionId) throws ApiException {
		validateUser(userId);
		return retrievePromotionById(promotionId).map(this::buildPromotionResponse).orElseThrow(() -> {
			log.warn("Promotion was not found. Id: {}", promotionId);
			return new ApiException(AppErrorCode.PROMOTION_NOT_FOUND);
		});
	}

	public PromotionResponse updatePromotion(PromotionRequest request, String id) throws ApiException {
		validatePromotion(request);
		var promotionOpt = retrievePromotionById(id);
		if (promotionOpt.isEmpty()) {
			log.warn("Promotion was not found. Id: {}", request.userId());
			throw new ApiException(AppErrorCode.PROMOTION_NOT_FOUND);
		} else {
			var promotion = promotionOpt.get();
			var updatedPromotion = buildPromotion(request, promotion.id());
			promotionRepository.save(updatedPromotion);
			log.info("Promotion was successfully updated. Id: {}", updatedPromotion.id());
			return buildPromotionResponse(updatedPromotion);
		}
	}

	public void removePromotion(String promotionId, String userId) throws ApiException {
		validateUser(userId);
		var promotionOpt = retrievePromotionById(promotionId);
		if (promotionOpt.isPresent()) {
			try {
				promotionRepository.delete(promotionId);
			} catch (ExecutionException | InterruptedException e) {
				log.error("Failed to delete a promotion from DB by id {}.", promotionId, e);
				throw new ApiException(AppErrorCode.INTERNAL_DATABASE_COMMUNICATION_ERROR);
			}
		} else {
			log.info("The provided promotion id was not found. id: {}", promotionId);
		}
	}

	/**
	 * Private methods
	 * ---------------------------------------------------------------------------------------------
	 */

	private void validatePromotion(PromotionRequest request) throws ApiException {
		validateUser(request.userId());
		var restaurantOpt = retrieveRestaurantById(request.restaurantId());
		if (!restaurantOpt.isPresent()) {
			throw new ApiException(AppErrorCode.RESTAURANT_NOT_FOUND);
		}
		var productOpt = restaurantOpt.get().products().stream()
				.filter(product -> product.id().equals(request.productId())).findFirst();
		if (!productOpt.isPresent()) {
			throw new ApiException(AppErrorCode.PRODUCT_NOT_FOUND);
		}
	}

	private void validateUser(String userId) throws ApiException {
		var userOpt = retrieveUserById(userId);
		if (userOpt.isEmpty()) {
			log.warn("User was not found. Id: {}", userId);
			throw new ApiException(AppErrorCode.USER_NOT_FOUND);
		} else {
			var user = userOpt.get();
			if (!User.UserType.RESTAURANT.equals(user.type())) {
				log.info("User provided is not valid for this operation. UserId: {}", userId);
				throw new ApiException(AppErrorCode.INVALID_USER_TYPE);
			}
		}
	}

	private Promotion buildPromotion(PromotionRequest request) throws ApiException {
		var id = UUID.randomUUID().toString();
		return buildPromotion(request, id);
	}

	private Promotion buildPromotion(PromotionRequest request, String id) throws ApiException {
		String category = retrieveRestaurantById(request.restaurantId()).get().products().stream()
				.filter(p -> p.id().equals(request.productId())).findFirst().get().category();
		return new Promotion(id, request.restaurantId(), request.productId(), request.price(), category);
	}

	private PromotionResponse buildPromotionResponse(Promotion promotion) {
		return new PromotionResponse(promotion.id(), promotion.restaurantId(), promotion.productId(),
				promotion.price());
	}

	private List<Promotion> retrievePromotions() throws ApiException {
		try {
			return promotionRepository.getAll();
		} catch (ExecutionException | InterruptedException e) {
			log.error("Failed to read all promotions from DB.", e);
			throw new ApiException(AppErrorCode.INTERNAL_DATABASE_COMMUNICATION_ERROR);
		}
	}

	private Optional<Promotion> retrievePromotionById(String id) throws ApiException {
		try {
			return promotionRepository.getById(id);
		} catch (ExecutionException | InterruptedException e) {
			log.error("Failed to read a restaurant from DB by id {}.", id, e);
			throw new ApiException(AppErrorCode.INTERNAL_DATABASE_COMMUNICATION_ERROR);
		}
	}

	private Optional<Restaurant> retrieveRestaurantById(String id) throws ApiException {
		try {
			return restaurantRepository.getById(id);
		} catch (ExecutionException | InterruptedException e) {
			log.error("Failed to read a restaurant from DB by id {}.", id, e);
			throw new ApiException(AppErrorCode.INTERNAL_DATABASE_COMMUNICATION_ERROR);
		}
	}

	private Optional<User> retrieveUserById(String id) throws ApiException {
		try {
			return userRepository.getById(id);
		} catch (ExecutionException | InterruptedException e) {
			log.error("Failed to read an user from DB by id {}.", id, e);
			throw new ApiException(AppErrorCode.INTERNAL_DATABASE_COMMUNICATION_ERROR);
		}
	}
}
