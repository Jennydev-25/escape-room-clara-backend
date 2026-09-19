package dev.jenny.clara.register;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.register.dtos.RegisterResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "${api-endpoint}/auth")
public class RegisterController {

    private final InterfaceRegisterService service;

    public RegisterController(InterfaceRegisterService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        RegisterResponseDTO responseDto = service.register(dto);
        return ResponseEntity.status(201).body(responseDto);
    }
}