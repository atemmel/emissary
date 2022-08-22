package emissarybackend;

import org.springframework.data.jpa.repository.JpaRepository;

interface ChatMessageAttachmentRepository extends JpaRepository<ChatMessageAttachment, Long> {
	
}
