package emissarybackend;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	CommandLineRunner initDatabase(EmissaryUserRepository userRepo, 
			ChatConversationRepository conversationRepo,
			ChatMessageRepository messageRepo) {

		// create everything
		final var conversation = new ChatConversation();

		final var users = Arrays.asList(
			new EmissaryUser("James"),
			new EmissaryUser("Greg"),
			new EmissaryUser("Dave"),
			new EmissaryUser("Sophie")
		);

		final var messages = Arrays.asList(
			new ChatMessage("Hello hello"),
			new ChatMessage("This is a message")
		);

		// add users to a conversation
		conversation.addParticipant(users.get(0));
		conversation.addParticipant(users.get(1));

		// add conversation to message
		conversation.setMessages(messages);

		return args -> {
			conversationRepo.save(conversation);
			users.forEach((user -> log.info("Preloading " + userRepo.save(user))));
			messages.forEach(msg -> messageRepo.save(msg));
		};
	}
}
