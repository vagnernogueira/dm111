package br.inatel.pos.dm111.vfp.publisher;

import br.inatel.pos.dm111.vfp.persistence.promotion.Promotion;

public interface AppPublisher {

	default Event buildEvent(Promotion promotion, Event.EventType eventType) {
		var promotionEvent = buildPromotionEvent(promotion);
		return new Event(eventType, promotionEvent);
	}

	default PromotionEvent buildPromotionEvent(Promotion promotion) {
		return new PromotionEvent(promotion.id(), promotion.restaurantId(), promotion.productId(), promotion.price());
	}

	boolean publishCreated(Promotion promotion);
}
