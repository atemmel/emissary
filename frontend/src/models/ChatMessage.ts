export interface ChatMessage {
  id: number|null;
  contents: string;
  author: number;
  conversation: number;
  timestamp: Date;
}
