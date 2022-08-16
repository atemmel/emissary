package emissarybackend;

import org.springframework.data.jpa.repository.JpaRepository;

interface EmissaryUserRepository extends JpaRepository<EmissaryUser, Long> {
	
}
