package br.inatel.pos.dm111.vfr.consumer;

public record PromotionEvent(String id, String restaurantId, String productId, float price) {
}
