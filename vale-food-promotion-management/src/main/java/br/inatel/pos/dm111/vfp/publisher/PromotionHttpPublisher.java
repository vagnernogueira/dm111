package br.inatel.pos.dm111.vfp.publisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.inatel.pos.dm111.vfp.persistence.promotion.Promotion;

@Profile("test")
@Component
public class PromotionHttpPublisher implements AppPublisher {
	@Value("${vale-food.user.url}")
	private String userUrl;

	@Value("${vale-food.restaurant.url}")
	private String restaurantUrl;

	private final RestTemplate restTemplate;

	public PromotionHttpPublisher(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public boolean publishCreated(Promotion promotion) {
		var event = buildEvent(promotion, Event.EventType.ADDED);
		restTemplate.postForObject(userUrl, event.event(), PromotionEvent.class);
		restTemplate.postForObject(restaurantUrl, event.event(), PromotionEvent.class);
		return true;
	}
}
