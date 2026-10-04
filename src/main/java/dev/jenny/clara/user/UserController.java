package dev.jenny.clara.user;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.user.dtos.UpdateProfileRequestDTO;
import dev.jenny.clara.user.dtos.UserProfileResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "${api-endpoint}/users/me")
public class UserController {

    private final InterfaceUserService userService;

    public UserController(InterfaceUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserProfileResponseDTO getProfile(Authentication authentication) {
        return userService.getProfile(authentication);
    }

    @PutMapping
    public UserProfileResponseDTO updateProfile(Authentication authentication, @Valid @RequestBody UpdateProfileRequestDTO dto) {
        return userService.updateProfile(authentication, dto);
    }
}