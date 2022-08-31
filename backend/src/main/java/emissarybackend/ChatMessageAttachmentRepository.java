package emissarybackend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ChatMessageAttachmentRepository extends JpaRepository<ChatMessageAttachment, Long> {
	
}
