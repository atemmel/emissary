package emissarybackend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollVoteRepository extends JpaRepository<PollVote, Long> {
	public List<PollVote> findByPoll(ChatMessage message);
}
