export interface FriendsListItem {
	conversationId: number;
	friendName: string;
	prevMessage: string;
	prevAuthor: string;
	timestamp: Date;
}

export interface ConversationHead {
	lastFetch: Date;
	head: Date;
}
