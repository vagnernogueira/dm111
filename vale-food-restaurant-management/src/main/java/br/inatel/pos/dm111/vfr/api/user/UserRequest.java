package br.inatel.pos.dm111.vfr.api.user;

import java.util.List;

public record UserRequest(String id, String name, String email, String type, List<String> preferredCategories) {
}
