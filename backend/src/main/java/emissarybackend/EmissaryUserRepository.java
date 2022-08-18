package emissarybackend;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface EmissaryUserRepository extends JpaRepository<EmissaryUser, Long> {
	public Optional<EmissaryUser> findByName(String name);
}
