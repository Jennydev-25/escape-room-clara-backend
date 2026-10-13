package dev.jenny.clara.trash.mappers;

import dev.jenny.clara.trash.DiscardedFileEntity;
import dev.jenny.clara.trash.dtos.DiscardedFileDTO;

public class TrashMapper {

    private TrashMapper() {
        throw new UnsupportedOperationException("No se puede instanciar esta clase de utilidad");
    }

    public static DiscardedFileDTO toDTO(DiscardedFileEntity file) {
        return new DiscardedFileDTO(file.getId(), file.getName(), file.getType(), file.getBody());
    }
}
