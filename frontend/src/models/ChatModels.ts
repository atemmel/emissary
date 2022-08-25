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
