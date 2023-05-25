import com.ithirteeng.messengerapi.chat.dto.message.FindMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.SendChatMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.SendDialogueMessageDto;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.ChatUserEntity;
import com.ithirteeng.messengerapi.chat.repository.ChatRepository;
import com.ithirteeng.messengerapi.chat.repository.ChatUserRepository;
import com.ithirteeng.messengerapi.chat.repository.MessageRepository;
import com.ithirteeng.messengerapi.chat.service.CommonService;
import com.ithirteeng.messengerapi.chat.service.FileService;
import com.ithirteeng.messengerapi.chat.service.MessageService;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.UserDto;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MessageServiceTest {

    @Mock
    private CommonService commonService;

    @Mock
    private ChatUserRepository chatUserRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private FileService fileService;

    @InjectMocks
    private MessageService messageService;

    @Test
    public void testSendMessageInDialogue() {
        var userId = UUID.randomUUID();
        var externalId = UUID.randomUUID();
        SendDialogueMessageDto sendDialogueMessageDto = SendDialogueMessageDto.builder()
                .message("test")
                .UserId(externalId)
                .filesIdsList(new ArrayList<>())
                .build();

        doNothing().when(commonService).checkUserExisting(any(UUID.class));
        doNothing().when(commonService).checkIfUserInBlackList(any(UUID.class), any(UUID.class));
        when(commonService.checkIfFileExists(any(String.class))).thenReturn(true);
        when(commonService.getUserById(any(UUID.class))).thenReturn(new UserDto());
        doNothing().when(fileService).attachFilesToMessage(any(List.class), any(UUID.class));

        messageService.sendMessageInDialogue(sendDialogueMessageDto, userId);
    }

    @Test(expected = BadRequestException.class)
    public void testSendMessageInDialogue_ThrowsBadRequestException() {
        var userId = UUID.randomUUID();
        var externalId = userId;
        SendDialogueMessageDto sendDialogueMessageDto = SendDialogueMessageDto.builder()
                .message("test")
                .UserId(externalId)
                .filesIdsList(new ArrayList<>())
                .build();

        doNothing().when(commonService).checkUserExisting(any(UUID.class));
        doNothing().when(commonService).checkIfUserInBlackList(any(UUID.class), any(UUID.class));
        when(commonService.checkIfFileExists(any(String.class))).thenReturn(true);
        when(commonService.getUserById(any(UUID.class))).thenReturn(new UserDto());
        doNothing().when(fileService).attachFilesToMessage(any(List.class), any(UUID.class));

        messageService.sendMessageInDialogue(sendDialogueMessageDto, userId);
    }

    @Test
    public void testSendMessageInChat() {
        SendChatMessageDto sendChatMessageDto = SendChatMessageDto.builder()
                .chatId(UUID.randomUUID())
                .message("sdfsdf")
                .filesIdsList(new ArrayList<>())
                .build();
        when(chatRepository.findById(any(UUID.class))).thenReturn(
                Optional.of(ChatEntity.builder().isDialog(true).build())
        );
        when(chatUserRepository.existsChatUserByUserIdAndChatEntity(any(UUID.class), any(ChatEntity.class))).thenReturn(true);

        doNothing().when(commonService).checkUserExisting(any(UUID.class));
        doNothing().when(commonService).checkIfUserInBlackList(any(UUID.class), any(UUID.class));
        when(commonService.checkIfFileExists(any(String.class))).thenReturn(true);
        when(commonService.getUserById(any(UUID.class))).thenReturn(new UserDto());
        doNothing().when(fileService).attachFilesToMessage(any(List.class), any(UUID.class));

        ChatUserEntity chatUserEntity = ChatUserEntity.builder()
                .chatEntity(new ChatEntity())
                .userId(UUID.randomUUID())
                .id(UUID.randomUUID())
                .build();

        when(chatUserRepository.findAllByChatEntity(any(ChatEntity.class))).thenReturn(
                Lists.list(chatUserEntity, chatUserEntity)
        );

        messageService.sendMessageInChat(sendChatMessageDto, UUID.randomUUID());
    }

    @Test(expected = BadRequestException.class)
    public void testSendMessageInChat_ThrowsBadRequestException() {
        SendChatMessageDto sendChatMessageDto = SendChatMessageDto.builder()
                .chatId(UUID.randomUUID())
                .message("sdfsdf")
                .filesIdsList(new ArrayList<>())
                .build();
        when(chatRepository.findById(any(UUID.class))).thenReturn(
                Optional.of(ChatEntity.builder().isDialog(true).build())
        );
        when(chatUserRepository.existsChatUserByUserIdAndChatEntity(any(UUID.class), any(ChatEntity.class))).thenReturn(false);
        messageService.sendMessageInChat(sendChatMessageDto, UUID.randomUUID());
    }

    @Test(expected = NotFoundException.class)
    public void testSendMessageInChat_ThrowsNotFoundException() {
        SendChatMessageDto sendChatMessageDto = SendChatMessageDto.builder()
                .chatId(UUID.randomUUID())
                .message("sdfsdf")
                .filesIdsList(new ArrayList<>())
                .build();
        when(chatRepository.findById(any(UUID.class))).thenReturn(
                Optional.empty()
        );
        messageService.sendMessageInChat(sendChatMessageDto, UUID.randomUUID());
    }

    @Test
    public void testGetMessagesByTest() {
        FindMessageDto findMessageDto = new FindMessageDto("");

        when(chatUserRepository.findAllByUserId(any(UUID.class))).thenReturn(new ArrayList<>());
        when(messageRepository.findAllByMessageTextLikeByOrderByCreationDateAsc(any(String.class))).thenReturn(new ArrayList<>());

        messageService.getMessagesByText(findMessageDto, UUID.randomUUID());
    }

    @Test
    public void testGetMessagesList() {
        when(chatRepository.findById(any(UUID.class))).thenReturn(Optional.of(
                ChatEntity.builder()
                        .id(UUID.randomUUID())
                        .build()
        ));

        when(chatUserRepository.existsChatUserByUserIdAndChatEntity(any(UUID.class), any(ChatEntity.class))).thenReturn(true);
        when(messageRepository.findAllByChatEntityOrderByCreationDateDesc(any(ChatEntity.class))).thenReturn(new ArrayList<>());

        var result = messageService.getMessagesList(UUID.randomUUID(), UUID.randomUUID());

        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetMessagesList_ThrowsNotFoundException() {
        when(chatRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        var result = messageService.getMessagesList(UUID.randomUUID(), UUID.randomUUID());

        assertNotNull(result);
    }

    @Test(expected = BadRequestException.class)
    public void testGetMessagesList_ThrowsBadRequestException() {
        when(chatRepository.findById(any(UUID.class))).thenReturn(Optional.of(
                ChatEntity.builder()
                        .id(UUID.randomUUID())
                        .build()
        ));

        when(chatUserRepository.existsChatUserByUserIdAndChatEntity(any(UUID.class), any(ChatEntity.class))).thenReturn(false);
        when(messageRepository.findAllByChatEntityOrderByCreationDateDesc(any(ChatEntity.class))).thenReturn(new ArrayList<>());
messageService.getMessagesList(UUID.randomUUID(), UUID.randomUUID());

    }

}
