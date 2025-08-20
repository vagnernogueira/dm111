package br.inatel.pos.dm111.vfp.api.restaurant;

public record ProductResponse(String id,
                              String name,
                              String description,
                              String category,
                              float price) {
}
