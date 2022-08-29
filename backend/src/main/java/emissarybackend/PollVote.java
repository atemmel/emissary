package emissarybackend; 

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class PollVote {
	private @Id @GeneratedValue Long id;

	@ManyToOne
	@JoinColumn(name = "VOTE_POLL_ID")
	@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class, 
		property = "id", 
		resolver = EntityIdResolver.class, 
		scope = ChatMessage.class)
	@JsonIdentityReference(alwaysAsId = true)
	private ChatMessage poll;

	@ManyToOne
	@JoinColumn(name = "VOTE_AUTHOR_ID")
	@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class, 
		property = "id", 
		resolver = EntityIdResolver.class, 
		scope = EmissaryUser.class)
	@JsonIdentityReference(alwaysAsId = true)
	private EmissaryUser author;

	private String choice;

	public PollVote() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ChatMessage getPoll() {
		return poll;
	}

	public void getPoll(ChatMessage poll) {
		this.poll = poll;
	}

	public EmissaryUser getAuthor() {
		return author;
	}

	public void setAuthor(EmissaryUser author) {
		this.author = author;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}
}
