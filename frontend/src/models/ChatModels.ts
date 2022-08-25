export interface ChatMessage {
  id: number|null;
  contents: string;
  author: number;
  conversation: number;
  timestamp: Date;
  attachment: ChatMessageAttachment|null;
}

export interface ChatMessageAttachment {
	name: string;
	type: string;
	bytes: string;
}

export interface ConversationHeads {
	[key: number]: number
}

export interface ChatHead {
	conversationHeads: ConversationHeads
	friendsListHead: Date;
}

export interface ChatCache {
	messages: ChatMessage[];
	timestamp: Date;
}

export interface ChatCaches {
	[key: number]: ChatCache;
}
