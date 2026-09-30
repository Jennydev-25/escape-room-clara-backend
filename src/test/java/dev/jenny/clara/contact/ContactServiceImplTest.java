package dev.jenny.clara.contact;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.contact.dtos.ContactResponseDTO;
import dev.jenny.clara.recaptcha.RecaptchaService;
import dev.jenny.clara.recaptcha.exceptions.InvalidRecaptchaException;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @InjectMocks
    private ContactServiceImpl service;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private RecaptchaService recaptchaService;

    @Test
    void testSend_ShouldSaveContactMessageAndReturnConfirmation() {
        ContactRequestDTO dtoRequest = new ContactRequestDTO(
                "Jugador de prueba",
                "jugador@pruebas.com",
                ContactType.QUESTION,
                "No encuentro dónde seguir en la carpeta del incendio.",
                "valid-captcha-token");

        when(recaptchaService.verify("valid-captcha-token")).thenReturn(true);

        ContactResponseDTO response = service.send(dtoRequest);

        ArgumentCaptor<ContactMessage> messageCaptor = ArgumentCaptor.forClass(ContactMessage.class);
        verify(contactRepository).save(messageCaptor.capture());
        ContactMessage savedMessage = messageCaptor.getValue();

        assertThat(savedMessage.getName(), is(equalTo("Jugador de prueba")));
        assertThat(savedMessage.getEmail(), is(equalTo("jugador@pruebas.com")));
        assertThat(savedMessage.getType(), is(equalTo(ContactType.QUESTION)));
        assertThat(savedMessage.getMessage(), is(equalTo("No encuentro dónde seguir en la carpeta del incendio.")));
        assertThat(response.message(),
                is(equalTo("Mensaje recibido correctamente. Tendrás respuesta en menos de 24/48 horas.")));
    }

    @Test
    void testSend_ShouldThrowInvalidRecaptchaException_WhenTokenIsInvalid() {
        ContactRequestDTO dtoRequest = new ContactRequestDTO(
                "Jugador de prueba",
                "jugador@pruebas.com",
                ContactType.QUESTION,
                "No encuentro dónde seguir en la carpeta del incendio.",
                "invalid-captcha-token");

        when(recaptchaService.verify("invalid-captcha-token")).thenReturn(false);

        assertThrows(InvalidRecaptchaException.class, () -> service.send(dtoRequest));

        verify(contactRepository, never()).save(any());
    }
}