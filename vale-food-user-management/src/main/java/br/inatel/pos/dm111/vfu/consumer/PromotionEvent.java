package br.inatel.pos.dm111.vfu.consumer;

public record PromotionEvent(String id, String restaurantId, String productId, float price) {
}
