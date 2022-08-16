package emissarybackend;

import org.springframework.data.jpa.repository.JpaRepository;

interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {

}
