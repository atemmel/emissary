package emissarybackend;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FriendsListItemController {
	private final FriendsListItemService service;

	FriendsListItemController(FriendsListItemService service) {
		this.service = service;
	}

	@GetMapping("/friendslistitems/{userId}")
	List<FriendsListItem> one(@PathVariable Long userId) {
		return service.createFriendsListItemsByUserId(userId);
	}
}
