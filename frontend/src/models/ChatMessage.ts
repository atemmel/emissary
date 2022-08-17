export interface ChatMessage {
  id: number;
  contents: string;
  author: number;
  conversation: number;
  //sequence: number, // chat message ordering
  //timestamp
}
