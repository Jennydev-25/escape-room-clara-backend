package dev.jenny.clara.seen.mappers;

import dev.jenny.clara.seen.SeenItemEntity;
import dev.jenny.clara.seen.dtos.SeenItemResponseDTO;

public class SeenItemMapper {

    private SeenItemMapper() {
        throw new UnsupportedOperationException("No se puede instanciar esta clase de utilidad");
    }

    public static SeenItemResponseDTO toDTO(SeenItemEntity item) {
        return new SeenItemResponseDTO(item.getItemKey(), item.getSeenAt());
    }
}
