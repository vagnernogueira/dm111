package br.inatel.pos.dm111.vfp.publisher;

public record PromotionEvent(String id,
                        String restaurantId,
                        String productId,
                        float price) {
}
