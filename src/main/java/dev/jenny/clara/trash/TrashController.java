package dev.jenny.clara.trash;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.trash.dtos.DiscardedFileDTO;

@RestController
@RequestMapping(path = "${api-endpoint}/trash")
public class TrashController {

    private final InterfaceTrashService trashService;

    public TrashController(InterfaceTrashService trashService) {
        this.trashService = trashService;
    }

    @GetMapping
    public List<DiscardedFileDTO> listDiscardedFiles(Authentication authentication) {
        return trashService.listDiscardedFiles(authentication);
    }
}
