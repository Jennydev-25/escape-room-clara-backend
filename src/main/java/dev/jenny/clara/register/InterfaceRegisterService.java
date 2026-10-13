package dev.jenny.clara.register;

import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.register.dtos.RegisterResponseDTO;

public interface InterfaceRegisterService {

    RegisterResponseDTO register(RegisterRequestDTO request);

}