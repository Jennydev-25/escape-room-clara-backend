package dev.jenny.clara.recaptcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.jenny.clara.recaptcha.dtos.RecaptchaVerifyResponseDTO;
import dev.jenny.clara.recaptcha.exceptions.InvalidRecaptchaException;

@Service
public class RecaptchaServiceImpl implements InterfaceRecaptchaService {

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify?secret={secret}&response={response}";

    private final RestTemplate restTemplate;
    private final String secretKey;

    public RecaptchaServiceImpl(RestTemplate restTemplate, @Value("${recaptcha.secret-key}") String secretKey) {
        this.restTemplate = restTemplate;
        this.secretKey = secretKey;
    }

    @Override
    public boolean verify(String token) {
        RecaptchaVerifyResponseDTO response = restTemplate.postForObject(VERIFY_URL, null,
                RecaptchaVerifyResponseDTO.class, secretKey, token);
        return response != null && response.success();
    }

    @Override
    public void verifyOrThrow(String token) {
        if (!verify(token)) {
            throw new InvalidRecaptchaException("El token de recaptcha no es válido.");
        }
    }
}