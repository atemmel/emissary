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
	CommandLineRunner initDatabase(
			EmissaryUserRepository userRepo, 
			ChatConversationRepository conversationRepo,
			ChatMessageRepository messageRepo) {

		log.info("Preloading begin...");

		// create everything
		var conversations = conversationRepo.saveAll(Arrays.asList(
			new ChatConversation(),
			new ChatConversation()
		));

		final var users = userRepo.saveAll(Arrays.asList(
			new EmissaryUser("James"),
			new EmissaryUser("Greg"),
			new EmissaryUser("Dave"),
			new EmissaryUser("Sophie")
		));

		final var messages = Arrays.asList(
			messageRepo.saveAll(Arrays.asList(
				new ChatMessage("Hello hello", users.get(0)),
				new ChatMessage("This is a message", users.get(1))
			)),
			messageRepo.saveAll(Arrays.asList(
				new ChatMessage("Second conversation", users.get(0)),
				new ChatMessage("Very good", users.get(2))
			))
		);

		// add users to a conversation
		conversations.get(0).addParticipant(users.get(0));
		conversations.get(0).addParticipant(users.get(1));

		conversations.get(1).addParticipant(users.get(0));
		conversations.get(1).addParticipant(users.get(2));

		// add conversations to messages
		assert(conversations.size() == messages.size());
		for(int i = 0; i < conversations.size(); i++) {
			conversations.get(i).setMessages(messages.get(i));
		}

		conversations = conversationRepo.saveAll(conversations);

		// add messages to conversations
		for(int i = 0; i < conversations.size(); i++) {
			var msg = messages.get(i);
			for (int j = 0; j < msg.size(); j++) {
				msg.get(j).setConversation(conversations.get(i));
			}
			messageRepo.saveAll(msg);
		}

		return args -> {
			log.info("Preloading complete!");
		};
	}
}
