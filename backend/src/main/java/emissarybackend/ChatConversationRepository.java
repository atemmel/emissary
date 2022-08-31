package emissarybackend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {

}
