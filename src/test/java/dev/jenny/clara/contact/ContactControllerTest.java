package dev.jenny.clara.contact;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jenny.clara.config.SecurityConfig;
import dev.jenny.clara.contact.dtos.ContactRequestDTO;
import dev.jenny.clara.contact.dtos.ContactResponseDTO;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ContactController.class)
@Import(SecurityConfig.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterfaceContactService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testSend_ShouldReturnCreated() throws Exception {
        ContactRequestDTO requestDto = new ContactRequestDTO("Jugador de prueba", "jugador@pruebas.com",
                ContactType.QUESTION, "No encuentro dónde seguir en la carpeta del incendio.", "valid-captcha-token");
        ContactResponseDTO responseDto = new ContactResponseDTO(
                "Mensaje recibido correctamente. Tendrás respuesta en menos de 24/48 horas.");
        String requestJson = mapper.writeValueAsString(requestDto);
        String responseJson = mapper.writeValueAsString(responseDto);

        when(service.send(requestDto, null)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(equalTo(201)));
        assertThat(response.getContentAsString(), is(equalTo(responseJson)));
    }
}