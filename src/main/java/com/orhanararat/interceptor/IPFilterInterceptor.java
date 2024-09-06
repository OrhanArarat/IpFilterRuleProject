package com.orhanararat.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class IPFilterInterceptor implements HandlerInterceptor {

    private final KieContainer kieContainer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        KieSession kieSession = kieContainer.newKieSession();
        String sourceIp = request.getRemoteAddr();
        String destinationIp = request.getLocalAddr();

        kieSession.insert(sourceIp);
        kieSession.insert(destinationIp);
        kieSession.fireAllRules();

        boolean allowed = kieSession.getObjects(o -> o instanceof Boolean)
                .stream()
                .map(o -> (Boolean) o)
                .findFirst()
                .orElse(false);

        if (!allowed) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return false;
        }

        return true;
    }
}
