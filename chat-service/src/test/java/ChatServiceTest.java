import com.ithirteeng.messengerapi.chat.dto.chat.CreateChatDto;
import com.ithirteeng.messengerapi.chat.dto.chat.InputChatPageDto;
import com.ithirteeng.messengerapi.chat.dto.chat.UpdateChatDto;
import com.ithirteeng.messengerapi.chat.dto.common.PageInfoDto;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.ChatUserEntity;
import com.ithirteeng.messengerapi.chat.repository.ChatRepository;
import com.ithirteeng.messengerapi.chat.repository.ChatUserRepository;
import com.ithirteeng.messengerapi.chat.repository.MessageRepository;
import com.ithirteeng.messengerapi.chat.service.ChatService;
import com.ithirteeng.messengerapi.chat.service.CommonService;
import com.ithirteeng.messengerapi.chat.service.PaginationHelperService;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.UserDto;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ChatServiceTest {
    @Mock
    private ChatUserRepository chatUserRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private CommonService commonService;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ChatService chatService;

    @Test
    public void testCreateChat() {
        var userId = UUID.randomUUID();
        List<UUID> usersList = Lists.list(UUID.randomUUID(), UUID.randomUUID());
        CreateChatDto createChatDto = CreateChatDto.builder()
                .chatName("testName")
                .avatarId(UUID.randomUUID())
                .usersIdsList(usersList)
                .build();

        when(commonService.checkIfFileExists(createChatDto.getAvatarId().toString())).thenReturn(true);
        when(chatUserRepository.existsChatUserByUserIdAndChatEntity(any(UUID.class), any(ChatEntity.class))).thenReturn(false);

        var result = chatService.createChat(createChatDto, userId);

        assertNotNull(result);
    }

    @Test(expected = BadRequestException.class)
    public void testCreateChat_ThrowsBadRequestException() {
        var userId = UUID.randomUUID();
        List<UUID> usersList = Lists.list(UUID.randomUUID());
        CreateChatDto createChatDto = CreateChatDto.builder()
                .chatName("testName")
                .avatarId(UUID.randomUUID())
                .usersIdsList(usersList)
                .build();

        when(commonService.checkIfFileExists(createChatDto.getAvatarId().toString())).thenReturn(true);
        when(chatUserRepository.existsChatUserByUserIdAndChatEntity(any(UUID.class), any(ChatEntity.class))).thenReturn(false);
        chatService.createChat(createChatDto, userId);
    }

    @Test(expected = NotFoundException.class)
    public void testCreateChat_ThrowsNotFoundException() {
        var userId = UUID.randomUUID();
        List<UUID> usersList = Lists.list(UUID.randomUUID(), UUID.randomUUID());
        CreateChatDto createChatDto = CreateChatDto.builder()
                .chatName("testName")
                .avatarId(UUID.randomUUID())
                .usersIdsList(usersList)
                .build();

        when(commonService.checkIfFileExists(createChatDto.getAvatarId().toString())).thenReturn(false);
        when(chatUserRepository.existsChatUserByUserIdAndChatEntity(any(UUID.class), any(ChatEntity.class))).thenReturn(false);

        var result = chatService.createChat(createChatDto, userId);

        assertNotNull(result);
    }

    @Test
    public void testUpdateChat() {
        var userId = UUID.randomUUID();
        List<UUID> usersList = Lists.list(UUID.randomUUID(), UUID.randomUUID());
        UpdateChatDto updateChatDto = UpdateChatDto.builder()
                .chatId(UUID.randomUUID())
                .chatName("testName")
                .usersIdsList(usersList)
                .build();

        ChatEntity chatEntity = ChatEntity.builder()
                .id(updateChatDto.getChatId())
                .chatAdmin(userId)
                .chatName(updateChatDto.getChatName())
                .isDialog(false)
                .chatUserEntitiesList(new ArrayList<>())
                .creationDate(new Date())
                .build();

        when(chatRepository.findById(any(UUID.class))).thenReturn(Optional.of(chatEntity));
        when(chatUserRepository.existsChatUserByUserIdAndChatEntity(any(UUID.class), any(ChatEntity.class))).thenReturn(true);

        var result = chatService.updateChat(updateChatDto, userId);

        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateChat_ThrowsNotFoundException() {
        var userId = UUID.randomUUID();
        List<UUID> usersList = Lists.list(UUID.randomUUID(), UUID.randomUUID());
        UpdateChatDto updateChatDto = UpdateChatDto.builder()
                .chatId(UUID.randomUUID())
                .chatName("testName")
                .usersIdsList(usersList)
                .build();

        when(chatRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(chatUserRepository.existsChatUserByUserIdAndChatEntity(any(UUID.class), any(ChatEntity.class))).thenReturn(true);
        chatService.updateChat(updateChatDto, userId);
    }

    @Test
    public void testGetChatInfoById() {
        ChatEntity chatEntity = ChatEntity.builder()
                .id(UUID.randomUUID())
                .chatAdmin(UUID.randomUUID())
                .chatName("testName")
                .isDialog(true)
                .chatUserEntitiesList(new ArrayList<>())
                .creationDate(new Date())
                .build();
        when(chatRepository.findById(any(UUID.class))).thenReturn(Optional.of(chatEntity));
        when(chatUserRepository.existsChatUserByUserIdAndChatEntity(any(UUID.class), any(ChatEntity.class))).thenReturn(true);

        List<ChatUserEntity> chatsList = Lists.list(new ChatUserEntity(UUID.randomUUID(), UUID.randomUUID(), chatEntity));
        when(chatUserRepository.findAllByChatEntity(any(ChatEntity.class))).thenReturn(chatsList);
        when(commonService.getUserById(any(UUID.class))).thenReturn(new UserDto());

        var result = chatService.getChatInfoById(UUID.randomUUID(), UUID.randomUUID());

        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetChatInfoById_ThrowsNotFoundException() {
        when(chatRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        var result = chatService.getChatInfoById(UUID.randomUUID(), UUID.randomUUID());

        assertNotNull(result);
    }

    @Test(expected = BadRequestException.class)
    public void testGetChatInfoById_ThrowsBadRequestException() {
        ChatEntity chatEntity = ChatEntity.builder()
                .id(UUID.randomUUID())
                .chatAdmin(UUID.randomUUID())
                .chatName("testName")
                .isDialog(true)
                .chatUserEntitiesList(new ArrayList<>())
                .creationDate(new Date())
                .build();
        when(chatRepository.findById(any(UUID.class))).thenReturn(Optional.of(chatEntity));
        when(chatUserRepository.existsChatUserByUserIdAndChatEntity(any(UUID.class), any(ChatEntity.class))).thenReturn(false);

        List<ChatUserEntity> chatsList = Lists.list(new ChatUserEntity(UUID.randomUUID(), UUID.randomUUID(), chatEntity));
        when(chatUserRepository.findAllByChatEntity(any(ChatEntity.class))).thenReturn(chatsList);
        when(commonService.getUserById(any(UUID.class))).thenReturn(new UserDto());
        chatService.getChatInfoById(UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    public void testGetPage() {
        PageInfoDto pageInfoDto = new PageInfoDto(1, 10);
        InputChatPageDto inputChatPageDto = new InputChatPageDto(pageInfoDto, "");
        ChatEntity chatEntity = ChatEntity.builder()
                .id(UUID.randomUUID())
                .chatAdmin(UUID.randomUUID())
                .chatName("testName")
                .isDialog(true)
                .chatUserEntitiesList(new ArrayList<>())
                .creationDate(new Date())
                .build();
        List<ChatUserEntity> usersChatsList = Lists.list(new ChatUserEntity(UUID.randomUUID(), UUID.randomUUID(), chatEntity));
        List<ChatEntity>  chatsList = Lists.list(chatEntity);

        try {
            mockStatic(PaginationHelperService.class);
        } catch (Exception e){}

        when(chatUserRepository.findAllByUserId(any(UUID.class))).thenReturn(usersChatsList);
        when(PaginationHelperService.getCorrectPageList(chatsList, pageInfoDto)).thenReturn(chatsList);

        var result = chatService.getPage(inputChatPageDto, UUID.randomUUID());

        assertNotNull(result);
    }

}
