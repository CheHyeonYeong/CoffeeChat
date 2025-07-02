package com.backend.coffeeChat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(OutputCaptureExtension.class)
class AlimtalkServiceTest {

    private RestTemplate restTemplate;
    private AlimtalkService service;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        service = new AlimtalkService();
        ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(service, "apiKey", "key");
        ReflectionTestUtils.setField(service, "apiSecret", "secret");
    }

    @Test
    void sendAlimtalkSuccess(CapturedOutput output) {
        Map<String, String> vars = Map.of("name", "John");
        when(restTemplate.exchange(eq("https://api.solapi.com/messages/v4/send"), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("ok"));

        service.sendAlimtalk("010-1111-2222", "TEMPLATE", vars);

        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("https://api.solapi.com/messages/v4/send"), eq(HttpMethod.POST), entityCaptor.capture(), eq(String.class));

        HttpEntity<Map<String, Object>> captured = entityCaptor.getValue();
        HttpHeaders headers = captured.getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals("HMAC key:secret", headers.getFirst("Authorization"));

        Map<String, Object> expectedBody = new HashMap<>();
        expectedBody.put("to", "010-1111-2222");
        expectedBody.put("templateId", "TEMPLATE");
        expectedBody.put("kakaoOptions", Map.of("variables", vars));
        assertEquals(expectedBody, captured.getBody());

        assertTrue(output.getOut().contains("Alimtalk sent to 010-1111-2222 with template TEMPLATE"));
    }

    @Test
    void sendAlimtalkFailure(CapturedOutput output) {
        RestClientException ex = new RestClientException("fail");
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenThrow(ex);

        Map<String, String> vars = Map.of("key", "val");
        RestClientException thrown = assertThrows(RestClientException.class, () ->
                service.sendAlimtalk("010", "TEMP", vars));
        assertSame(ex, thrown);

        verify(restTemplate).exchange(anyString(), any(), any(), eq(String.class));
        assertTrue(output.getOut().contains("Failed to send Alimtalk to 010: fail"));
    }
}
