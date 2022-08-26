export type ChatMessage = {
  id: number|null;
  contents: string;
  author: number;
  conversation: number;
  timestamp: Date;
  attachment: ChatMessageAttachment|null;
}

export type ChatMessageAttachment = {
	name: string;
	type: string;
	bytes: string;
}

export type ConversationHeads = {
	[key: number]: number
}

export type ChatHead = {
	conversationHeads: ConversationHeads
	friendsListHead: Date;
}

export interface ChatCache {
	messages: ChatMessage[];
	timestamp: Date;
}

export type ChatCaches = Map<number, ChatCache>;
