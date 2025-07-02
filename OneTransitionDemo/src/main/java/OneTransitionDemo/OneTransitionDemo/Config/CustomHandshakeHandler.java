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
        // Use existing attribute or fallback to a random user
        Object userAttr = attributes.get("user");
        if (userAttr instanceof Principal p) {
            return p;
        }

        // fallback for anonymous users (dev mode)
        return () -> "user-" + UUID.randomUUID();
    }
}
