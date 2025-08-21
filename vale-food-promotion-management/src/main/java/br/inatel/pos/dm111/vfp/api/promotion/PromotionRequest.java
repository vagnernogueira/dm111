package br.inatel.pos.dm111.vfp.api.promotion;

public record PromotionRequest(String userId, String restaurantId, String productId, float price) {
}
