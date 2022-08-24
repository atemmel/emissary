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

export interface ChatHead {
	conversationHead: number;
	friendsListHead: Date;
}
