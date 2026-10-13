package dev.jenny.clara.recaptcha;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import dev.jenny.clara.recaptcha.dtos.RecaptchaVerifyResponseDTO;
import dev.jenny.clara.recaptcha.exceptions.InvalidRecaptchaException;

@ExtendWith(MockitoExtension.class)
class RecaptchaServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    private RecaptchaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RecaptchaServiceImpl(restTemplate, "test-secret-key");
    }

    @Test
    void testVerify_ShouldReturnTrue_WhenGoogleRespondsSuccess() {
        RecaptchaVerifyResponseDTO response = new RecaptchaVerifyResponseDTO(true);

        when(restTemplate.postForObject(anyString(), any(), eq(RecaptchaVerifyResponseDTO.class), anyString(),
                anyString()))
                .thenReturn(response);

        boolean result = service.verify("valid-token");

        assertThat(result, is(equalTo(true)));
    }

    @ParameterizedTest
    @MethodSource("failingResponses")
    void testVerify_ShouldReturnFalse_WhenGoogleFailsOrResponseIsNull(RecaptchaVerifyResponseDTO response) {
        when(restTemplate.postForObject(anyString(), any(), eq(RecaptchaVerifyResponseDTO.class), anyString(),
                anyString()))
                .thenReturn(response);

        boolean result = service.verify("some-token");

        assertThat(result, is(equalTo(false)));
    }

    @Test
    void testVerifyOrThrow_ShouldDoNothing_WhenTokenIsValid() {
        RecaptchaVerifyResponseDTO response = new RecaptchaVerifyResponseDTO(true);

        when(restTemplate.postForObject(anyString(), any(), eq(RecaptchaVerifyResponseDTO.class), anyString(),
                anyString()))
                .thenReturn(response);

        assertDoesNotThrow(() -> service.verifyOrThrow("valid-token"));
    }

    @Test
    void testVerifyOrThrow_ShouldThrowInvalidRecaptchaException_WhenTokenIsInvalid() {
        RecaptchaVerifyResponseDTO response = new RecaptchaVerifyResponseDTO(false);

        when(restTemplate.postForObject(anyString(), any(), eq(RecaptchaVerifyResponseDTO.class), anyString(),
                anyString()))
                .thenReturn(response);

        assertThrows(InvalidRecaptchaException.class, () -> service.verifyOrThrow("some-token"));
    }

    private static Stream<RecaptchaVerifyResponseDTO> failingResponses() {
        return Stream.of(
                new RecaptchaVerifyResponseDTO(false),
                null);
    }
}