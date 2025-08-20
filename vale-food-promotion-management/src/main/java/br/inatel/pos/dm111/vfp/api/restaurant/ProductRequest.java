package br.inatel.pos.dm111.vfp.api.restaurant;

public record ProductRequest(String name,
                             String description,
                             String category,
                             float price) {
}
