package br.inatel.pos.dm111.vfu.consumer;

public record Event(EventType type, PromotionEvent event) {

	public enum EventType {
		ADDED, UPDATED, DELETED
	};
}
