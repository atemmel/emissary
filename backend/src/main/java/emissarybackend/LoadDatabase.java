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

		// create everything
		final var conversations = Arrays.asList(
			new ChatConversation(),
			new ChatConversation()
		);

		final var users = Arrays.asList(
			new EmissaryUser("James"),
			new EmissaryUser("Greg"),
			new EmissaryUser("Dave"),
			new EmissaryUser("Sophie")
		);

		final var messages = Arrays.asList(
			Arrays.asList(
				new ChatMessage("Hello hello", users.get(0)),
				new ChatMessage("This is a message", users.get(1))
			),
			Arrays.asList(
				new ChatMessage("Second conversation", users.get(0)),
				new ChatMessage("Very good", users.get(0))
			)
		);

		// add users to a conversation
		conversations.get(0).addParticipant(users.get(0));
		conversations.get(0).addParticipant(users.get(1));

		/*
		 * This does not work (somehow)
		conversations.get(1).addParticipant(users.get(0));
		conversations.get(1).addParticipant(users.get(2));
		*/

		// add conversation to message
		assert(conversations.size() == messages.size());
		for(int i = 0; i < conversations.size(); i++) {
			conversations.get(i).setMessages(messages.get(i));
		}

		return args -> {
			log.info("Preloading begin...");

			conversations.forEach((conv -> conversationRepo.save(conv)));
			users.forEach((user -> userRepo.save(user)));
			messages.forEach((list -> list.forEach((msg -> messageRepo.save(msg)))));
			log.info("Preloading complete!");
		};
	}
}
