package dev.jenny.clara.seen.mappers;

import dev.jenny.clara.seen.SeenItemEntity;
import dev.jenny.clara.seen.dtos.SeenItemResponseDTO;

public class SeenItemMapper {

    public static SeenItemResponseDTO toDTO(SeenItemEntity item) {
        return new SeenItemResponseDTO(item.getItemKey(), item.getSeenAt());
    }
}
