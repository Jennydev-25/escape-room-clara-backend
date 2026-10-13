package dev.jenny.clara.recaptcha.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecaptchaVerifyResponseDTO(boolean success) {
}