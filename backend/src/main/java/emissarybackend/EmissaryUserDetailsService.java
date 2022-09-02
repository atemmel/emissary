package emissarybackend;

import org.springframework.stereotype.Component;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Component
public class EmissaryUserDetailsService implements UserDetailsService {
	@Autowired 
	private EmissaryUserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws EmissaryUserNotFoundException {
		EmissaryUser user = userRepo.findByName(username).orElseThrow(
				() -> EmissaryUserNotFoundException.fromName(username));
		return new User(
				user.getName(),
				user.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
	}
}
