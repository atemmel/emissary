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

		// if db lacks preload
		if(userRepo.count() <= 0) {
			// preload
			preload(userRepo, conversationRepo, messageRepo);
		} else {
			log.info("Database has contents, preloading skipped...");
		}

		return args -> {
			log.info("Preloading complete!");
		};
	}

	void preload(EmissaryUserRepository userRepo, 
			ChatConversationRepository conversationRepo,
			ChatMessageRepository messageRepo) {
		log.info("Preloading begin...");

		// create everything
		var conversations = conversationRepo.saveAll(Arrays.asList(
			new ChatConversation(),
			new ChatConversation()
		));

		final var users = userRepo.saveAll(Arrays.asList(
			new EmissaryUser("James", "$2a$12$mhIzeaakLilnHmcM9R/bN.pljqaiDYl3HDcsjMct4S.GOrHqbqhjy"), // James1
			new EmissaryUser("Greg", "$2a$12$vNqw4wJPGEsvpApS0BaF2e1OOelJtBaXXTBl1rudoeUK3f9.XM14u"), // Greg1
			new EmissaryUser("Dave", "$2a$12$TmuoDhdAiL0B3Xj0pJO9A.sLjlRmk3DNavAN34srGh.wV0Hz0G61S"), // Dave1
			new EmissaryUser("Sophie", "$2a$12$fWq9P1wddkf1KyAPHwAhBeijQ5m4FBLb2Wi4b9GLbWtBLLx87.32a") // Sophie1
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

	}
}
