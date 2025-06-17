import { Component, OnInit } from '@angular/core';
import { ChatService } from '../../services/chat.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  newReceiver = '';
  username = '';       
  message = '';
  chats: any[] = [];
  conversations: { userA: string; userB: string }[] = [];
  selectedConversation: { userA: string; userB: string } | null = null;

  editingChatId: string | null = null;
  editingMessage = '';

  constructor(
    private chatService: ChatService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.username = this.authService.getUsername() || '';
    this.loadChats();
  }

  loadChats() {
    this.chatService.getUserChats().subscribe(data => {
      this.chats = data.content
        .filter((chat: any) =>
          chat.senderUsername === this.username || chat.receiverUsername === this.username
        )
        .sort((a: any, b: any) =>
          new Date(a.sentAt).getTime() - new Date(b.sentAt).getTime()
        );

      this.extractConversations();

      if (this.conversations.length > 0 && !this.selectedConversation) {
        this.selectedConversation = this.conversations[0];
      }
    });
  }

  extractConversations() {
    const convSet = new Set<string>();
    this.conversations = [];

    this.chats.forEach(chat => {
      const users = [chat.senderUsername, chat.receiverUsername].sort();
      const key = users.join('||');

      if (!convSet.has(key)) {
        convSet.add(key);
        this.conversations.push({ userA: users[0], userB: users[1] });
      }
    });
  }

  selectConversation(conversation: { userA: string; userB: string }) {
    this.selectedConversation = conversation;
  }

  get filteredChats() {
    if (!this.selectedConversation) return [];
    const { userA, userB } = this.selectedConversation;
    return this.chats.filter(chat =>
      (chat.senderUsername === userA && chat.receiverUsername === userB) ||
      (chat.senderUsername === userB && chat.receiverUsername === userA)
    );
  }

  sendMessage() {
    let receiver = '';

    if (this.selectedConversation) {
      receiver = this.selectedConversation.userA === this.username
        ? this.selectedConversation.userB
        : this.selectedConversation.userA;
    }

    if (this.newReceiver.trim()) {
      receiver = this.newReceiver.trim();
    }

    if (this.username.trim() && this.message.trim() && receiver.trim()) {
      const newMessage = {
        senderUsername: this.username,
        receiverUsername: receiver,
        message: this.message,
        sentAt: new Date().toISOString(),
        id: 'temp-' + Date.now()
      };

      this.chats.push(newMessage);

      this.chatService.sendMessage(receiver, this.message).subscribe(() => {
        this.message = '';
        this.newReceiver = '';
        this.loadChats();
      });
    }
  }


  canEdit(chat: any): boolean {
    return chat.senderUsername === this.username;
  }

  startEditing(chat: any) {
    if (this.canEdit(chat)) {
      this.editingChatId = chat.id;
      this.editingMessage = chat.message;
    }
  }

  saveEdit(chatId: string) {
    if (this.editingMessage.trim()) {
      this.chatService.editMessage(chatId, this.editingMessage).subscribe(() => {
        this.editingChatId = null;
        this.editingMessage = '';
        this.loadChats();
      });
    }
  }

  cancelEdit() {
    this.editingChatId = null;
    this.editingMessage = '';
  }

  deleteMessage(chatId: string) {
    this.chatService.deleteMessage(chatId).subscribe(() => {
      this.loadChats();
    });
  }
}
