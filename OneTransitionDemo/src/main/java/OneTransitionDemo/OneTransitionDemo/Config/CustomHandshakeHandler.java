package OneTransitionDemo.OneTransitionDemo.Config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        // If Spring Security has set an authenticated principal, use it safely
        Principal principal = request.getPrincipal();
        if (principal != null && principal.getName() != null) {
            return principal;
        }

        // Otherwise, fallback
        return () -> "user-" + UUID.randomUUID();
    }

}
