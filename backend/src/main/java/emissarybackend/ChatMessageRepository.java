package emissarybackend;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	public Optional<ChatMessage> findByAttachment(ChatMessageAttachment attachment);
}
