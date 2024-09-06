package com.orhanararat.interceptor;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IPFilterInterceptorTest {

    @Mock
    private KieContainer kieContainer;

    @Mock
    private KieSession kieSession;

    @InjectMocks
    private IPFilterInterceptor interceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        when(kieContainer.newKieSession()).thenReturn(kieSession);
    }

    @Test
    void preHandle_AllowedAccess_ShouldReturnTrue() throws Exception {
        // Given
        String sourceIp = "192.168.1.1";
        String destinationIp = "10.0.0.1";
        request.setRemoteAddr(sourceIp);
        request.setLocalAddr(destinationIp);

        when(kieSession.getObjects(any())).thenAnswer(invocation -> Collections.singletonList(Boolean.TRUE));

        // When
        boolean result = interceptor.preHandle(request, response, null);

        // Then
        assertTrue(result);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        verify(kieSession).insert(sourceIp);
        verify(kieSession).insert(destinationIp);
        verify(kieSession).fireAllRules();
    }

    @Test
    void preHandle_DeniedAccess_ShouldReturnFalse() throws Exception {
        // Given
        String sourceIp = "192.168.1.1";
        String destinationIp = "10.0.0.1";
        request.setRemoteAddr(sourceIp);
        request.setLocalAddr(destinationIp);

        when(kieSession.getObjects(any())).thenAnswer(invocation -> Collections.singletonList(Boolean.FALSE));

        // When
        boolean result = interceptor.preHandle(request, response, null);

        // Then
        assertFalse(result);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        assertEquals("Access denied", response.getErrorMessage());
        verify(kieSession).insert(sourceIp);
        verify(kieSession).insert(destinationIp);
        verify(kieSession).fireAllRules();
    }

    @Test
    void preHandle_NoRuleResult_ShouldReturnFalse() throws Exception {
        // Given
        String sourceIp = "192.168.1.1";
        String destinationIp = "10.0.0.1";
        request.setRemoteAddr(sourceIp);
        request.setLocalAddr(destinationIp);

        when(kieSession.getObjects(any())).thenReturn(Collections.emptyList());

        // When
        boolean result = interceptor.preHandle(request, response, null);

        // Then
        assertFalse(result);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        assertEquals("Access denied", response.getErrorMessage());
        verify(kieSession).insert(sourceIp);
        verify(kieSession).insert(destinationIp);
        verify(kieSession).fireAllRules();
    }
}