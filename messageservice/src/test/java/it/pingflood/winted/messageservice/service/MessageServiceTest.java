package it.pingflood.winted.messageservice.service;

import it.pingflood.winted.messageservice.data.Message;
import it.pingflood.winted.messageservice.data.dto.MessageListResponse;
import it.pingflood.winted.messageservice.data.dto.MessageRequest;
import it.pingflood.winted.messageservice.data.dto.MessageResponse;
import it.pingflood.winted.messageservice.repository.MessageRepository;
import it.pingflood.winted.messageservice.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
  
  @Mock
  private MessageRepository messageRepository;
  
  @InjectMocks
  private MessageServiceImpl messageService;
  
  private Message message;
  private Message message1;
  private MessageListResponse messageListResponse;
  private MessageRequest messageRequest;
  private MessageResponse messageResponse;
  private LocalDateTime timeStamp;
  
  @BeforeEach
  void setup() {
    timeStamp = LocalDateTime.now();
    message = Message.builder().from("userid1").to("userid2").timestamp(timeStamp).content("Ciao!").build();
    message1 = Message.builder().from("userid3").to("userid2").timestamp(timeStamp).content("Ciao!").build();
    messageRequest = MessageRequest.builder().from("userid1").to("userid2").timestamp(timeStamp.toString()).content("Ciao!").build();
    messageResponse = MessageResponse.builder().from("userid1").to("userid2").timestamp(timeStamp.toString()).content("Ciao!").build();
    messageListResponse = MessageListResponse.builder().messageList(List.of(messageResponse)).build();
  }
  
  @DisplayName("Save - return correct value")
  @Test
  void when_save_message_is_should_be_return_message() {
    when(messageRepository.save(any())).thenReturn(message);
    MessageResponse mr = messageService.saveMessage(message.getFrom(), message.getTo(), messageRequest);
    assertThat(mr.getContent()).isSameAs(message.getContent());
  }
  
  // 2023-04-30T16:53:39.054034931
  // 2023-04-30T16:53:39
  @DisplayName("Save - throw exception when duplicate unique fields ")
  @Test
  void when_try_to_save_whit_existing_name_throws_exception() {
    when(messageRepository.findByFromEqualsIgnoreCaseAndToEqualsIgnoreCaseAndTimestampEquals(any(String.class), any(String.class), any(LocalDateTime.class)))
      .thenReturn(Optional.of(message));
    String to = message.getTo();
    String from = message.getFrom();
    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> messageService.saveMessage(from, to, messageRequest));
  }
  
  @DisplayName("Get All - return correct value")
  @Test
  void when_get_messages_should_be_return_messages() {
    // lenient().when.... bypassa l'eccezione "Potenziali problemi di stubbing" che in questo caso mi serve
    when(messageRepository.findAllByFromIsInAndToIsInOrderByTimestampDesc(List.of("userid1", "userid2"), List.of("userid1", "userid2"))).thenReturn(List.of(message, message, message));
    MessageListResponse messagesResponse = messageService.getConversation("userid1", "userid2");
    assertThat(messagesResponse.getMessageList())
      .isInstanceOf(List.class)
      .hasSize(3);
    assertThat(messagesResponse.getMessageList().get(0).getFrom())
      .isEqualTo(message.getFrom());
  }
  
  @DisplayName("Get One By From - return correct value")
  @Test
  void when_get_message_from_should_be_return_message() {
    when(messageRepository.findAllByFromIsIgnoreCase(any(String.class))).thenReturn(List.of(message, message));
    Map<String, List<MessageResponse>> messageResponse = messageService.getAllConversationFromUsername(message.getFrom());
    assertThat(messageResponse).containsKey(message.getTo());
  }
  
  
  @DisplayName("Get One By To - return correct value")
  @Test
  void when_get_message_to_should_be_return_message() {
    when(messageRepository.findAllByToIsIgnoreCase(any(String.class))).thenReturn(List.of(message1, message));
    Map<String, List<MessageResponse>> messageResponse = messageService.getAllConversationToUsername(message.getTo());
    assertThat(messageResponse).containsKey(message.getFrom());
  }

//  @DisplayName("Get One - throw exception when search not correct id")
//  @Test
//  void when_get_message_whit_wrong_id_should_throw() {
//    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> messageService.getOne("12344asasdk;sjdfkjs"));
//  }
//
//  @DisplayName("Get One - throw exception when search not present id")
//  @Test
//  void when_get_message_whit_not_present_id_should_throw() {
//    when(messageRepository.findById(any())).thenReturn(Optional.empty());
//    org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> messageService.getOne("644d35690b56f0720b01f1a9"));
//  }
//
//  @DisplayName("Put One - throw exception when try to change not correct id")
//  @Test
//  void when_put_message_whit_wrong_id_should_throw() {
//    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> messageService.updateMessage("12344asasdk;sjdfkjs", messagePutRequest));
//  }
//
//  @DisplayName("Put One - throw exception when try to change not present id")
//  @Test
//  void when_put_message_whit_not_present_id_should_throw() {
//    when(messageRepository.findById(any())).thenReturn(Optional.empty());
//    messagePutRequest.setId("644d35690b56f0720b01f1a9");
//    org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> messageService.updateMessage("644d35690b56f0720b01f1a9", messagePutRequest));
//  }
//
//  @DisplayName("Put One - throw exception when try to change mismatch id")
//  @Test
//  void when_put_message_whit_mismatch_id_should_throw() {
//    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> messageService.updateMessage("644d35690b56f0720b01f1a9", messagePutRequest));
//  }
//
//  @DisplayName("Put One - update message then return it")
//  @Test
//  void when_put_message_whit_should_return_it() {
//    Message updatedMessage = Message.builder().id(messagePutRequest.getId()).name(messagePutRequest.getName()).description(messagePutRequest.getDescription()).price(messagePutRequest.getPrice()).build();
//    when(messageRepository.save(updatedMessage)).thenReturn(message);
//    when(messageRepository.findById(messagePutRequest.getId())).thenReturn(Optional.of(message));
//    MessageResponse pr = messageService.updateMessage(messagePutRequest.getId(), messagePutRequest);
//    assertThat(pr.getName()).isEqualTo(updatedMessage.getName());
//  }
//
//
//  @DisplayName("Delete One - throw exception when try to delete not correct id")
//  @Test
//  void when_delete_message_whit_wrong_id_should_throw() {
//    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> messageService.deleteMessage("12344asasdk;sjdfkjs"));
//  }
//
//  @DisplayName("Delete One - throw exception when try to delete not present id")
//  @Test
//  void when_delete_message_whit_not_present_id_should_throw() {
//    when(messageRepository.findById(any())).thenReturn(Optional.empty());
//    org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> messageService.deleteMessage("644d35690b56f0720b01f1a9"));
//  }
}
