package dev.jenny.clara.register;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import dev.jenny.clara.register.dtos.RecaptchaVerifyResponseDTO;

@ExtendWith(MockitoExtension.class)
class RecaptchaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private RecaptchaService service;

    @BeforeEach
    void setUp() {
        service = new RecaptchaService(restTemplate, "test-secret-key");
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
}