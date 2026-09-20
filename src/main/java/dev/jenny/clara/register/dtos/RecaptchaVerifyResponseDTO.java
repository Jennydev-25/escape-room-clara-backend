package dev.jenny.clara.register.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecaptchaVerifyResponseDTO(boolean success) {
}