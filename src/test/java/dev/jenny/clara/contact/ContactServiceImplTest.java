package dev.jenny.clara.contact;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
import dev.jenny.clara.user.User;

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

        ContactResponseDTO response = service.send(dtoRequest, null);

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

        doThrow(new InvalidRecaptchaException("Invalid recaptcha token."))
                .when(recaptchaService).verifyOrThrow("invalid-captcha-token");

        assertThrows(InvalidRecaptchaException.class, () -> service.send(dtoRequest, null));

        verify(contactRepository, never()).save(any());
    }

    @Test
    void testSend_ShouldLinkContactMessageToUser_WhenUserIsAuthenticated() {
        ContactRequestDTO dtoRequest = new ContactRequestDTO(
                "Jugador de prueba",
                "jugador@pruebas.com",
                ContactType.QUESTION,
                "No encuentro dónde seguir en la carpeta del incendio.",
                "valid-captcha-token");
        User loggedInUser = User.builder().id(1L).email("jugador@pruebas.com").build();

        when(recaptchaService.verify("valid-captcha-token")).thenReturn(true);

        service.send(dtoRequest, loggedInUser);

        ArgumentCaptor<ContactMessage> messageCaptor = ArgumentCaptor.forClass(ContactMessage.class);
        verify(contactRepository).save(messageCaptor.capture());
        ContactMessage savedMessage = messageCaptor.getValue();

        assertThat(savedMessage.getUser(), is(equalTo(loggedInUser)));
    }
}