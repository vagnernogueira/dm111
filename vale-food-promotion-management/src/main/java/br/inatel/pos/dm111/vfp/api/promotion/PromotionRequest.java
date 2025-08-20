package br.inatel.pos.dm111.vfp.api.promotion;

public record PromotionRequest(String id, String restaurantId, String productId, float price) {
}
