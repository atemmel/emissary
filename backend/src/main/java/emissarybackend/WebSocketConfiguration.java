package emissarybackend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


@Configuration
@Controller
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/chat");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws/websocket")
			.setAllowedOriginPatterns("*")
			.addInterceptors(httpSessionHandshakeInterceptor());
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		//
		//
		// ============ This assumes base64 enc =============
		//
		//
		// 32301bytes (~32kb) file became 57456 bytes (~56kb)
		// 57456 / 32301 = ~1.77876 times increase
		// in other words, server should only accept files the size of 
		// 8 * 1.8 * 1024 * 1024 bytes

		//float realLimit = 8.f * 1.8f * 1024 * 1024;
		//int limit = Math.round(realLimit);
		//registration.setMessageSizeLimit(limit);
	}

	@Bean
	public HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor() {
		return new HttpSessionHandshakeInterceptor();
	}
}
