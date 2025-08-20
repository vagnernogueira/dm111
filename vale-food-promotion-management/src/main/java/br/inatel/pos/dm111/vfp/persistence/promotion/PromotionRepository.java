package br.inatel.pos.dm111.vfp.persistence.promotion;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.inatel.pos.dm111.vfp.persistence.ValeFoodRepository;

public interface PromotionRepository extends ValeFoodRepository<Promotion> {
    List<Promotion> findByRestaurant(String restaurantId) throws ExecutionException, InterruptedException;
}
