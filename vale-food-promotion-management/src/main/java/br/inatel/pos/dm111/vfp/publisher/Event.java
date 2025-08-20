package br.inatel.pos.dm111.vfp.publisher;

public record Event(EventType type, PromotionEvent event) {

	public enum EventType {
		ADDED, UPDATED, DELETED
	};
}
