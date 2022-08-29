package emissarybackend;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	public Optional<ChatMessage> findByAttachment(ChatMessageAttachment attachment);
}
