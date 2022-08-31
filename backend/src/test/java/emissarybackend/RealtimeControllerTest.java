package emissarybackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RealtimeControllerTest {

	@LocalServerPort
	private Integer port;

	WebSocketStompClient stompClient;

	String url() {
		return String.format("ws://localhost:%d/ws/websocket", port);
	}

	@BeforeEach
	public void setup() {

		stompClient = new WebSocketStompClient(new StandardWebSocketClient());
		stompClient.setMessageConverter(new MappingJackson2MessageConverter()); 
	}

	@Test
	void whenValidMessageThenSuccess() throws TimeoutException, ExecutionException, InterruptedException {
		/*
		var queue = new ArrayBlockingQueue<ChatMessage>(1);
		StompSession session = stompClient.connect(url(),
				new StompSessionHandlerAdapter() {}).get(1, TimeUnit.SECONDS);

		session.subscribe("/chat", new StompFrameHandler() {
           @Override
		   public Type getPayloadType(StompHeaders headers) {
				return ChatMessage.class;
		   }

		   @Override
		   public void handleFrame(StompHeaders headers, Object payload) {
				queue.add((ChatMessage)payload);
		   }
		});

		var a = new EmissaryUser();
		var b = new EmissaryUser();

		final var conversation = new ChatConversation();
		conversation.addParticipant(a);
		conversation.addParticipant(b);

		final var msg = new ChatMessage();
		msg.setContents("Hello");
		msg.setConversation(conversation);
		msg.setAuthor(a);

		session.send("/chat/send", msg);
		var response = queue.poll(1, TimeUnit.SECONDS);
		assertNotNull(response);
		assertEquals("Hello", response.getContents());
		*/
	}
}
