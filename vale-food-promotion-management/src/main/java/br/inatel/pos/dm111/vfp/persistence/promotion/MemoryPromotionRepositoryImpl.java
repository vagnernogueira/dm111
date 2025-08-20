package br.inatel.pos.dm111.vfp.persistence.promotion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MemoryPromotionRepositoryImpl implements PromotionRepository {

	private Map<String, Promotion> db = new HashMap<>();

	@Override
	public List<Promotion> findByRestaurant(String restaurantId) {
		return db.values().stream().filter(promotion -> promotion.restaurantId().equals(restaurantId))
				.collect(Collectors.toList());
	}

	@Override
	public List<Promotion> getAll() throws ExecutionException, InterruptedException {
		return db.values().stream().toList();
	}

	@Override
	public Optional<Promotion> getById(String id) throws ExecutionException, InterruptedException {
		return Optional.ofNullable(db.get(id));
	}

	@Override
	public Promotion save(Promotion promotion) {
		return db.put(promotion.id(), promotion);
	}

	@Override
	public void delete(String id) throws ExecutionException, InterruptedException {
		db.values().removeIf(promotion -> promotion.id().equals(id));
	}
}
