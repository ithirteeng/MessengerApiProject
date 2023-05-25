import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.service.CheckPaginationDetailsService;
import com.ithirteeng.messengerapi.friends.dto.common.InputPageDto;
import com.ithirteeng.messengerapi.friends.dto.common.PageFiltersDto;
import com.ithirteeng.messengerapi.friends.dto.common.SearchDto;
import com.ithirteeng.messengerapi.friends.dto.common.SortingDto;
import com.ithirteeng.messengerapi.friends.entity.BlockedUserEntity;
import com.ithirteeng.messengerapi.friends.repository.BlackListRepository;
import com.ithirteeng.messengerapi.friends.repository.FriendsRepository;
import com.ithirteeng.messengerapi.friends.service.BlackListService;
import com.ithirteeng.messengerapi.friends.service.CommonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BlackListServiceTest {
    @Mock
    private BlackListRepository blackListRepository;

    @Mock
    private FriendsRepository friendsRepository;

    @Mock
    private CommonService commonService;

    @Mock
    private CheckPaginationDetailsService paginationDetailsService;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private BlackListService blackListService;

    @Test
    public void testGetNoteData() {
        var friendId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        doNothing().when(commonService).checkUserExisting(any(UUID.class));

        when(blackListRepository.findByTargetUserIdAndAddingUserId(userId, friendId)).thenReturn(Optional.of(new BlockedUserEntity()));
        var result = blackListService.getNoteData(userId, friendId);

        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNoteData_ThrowsNotFoundException() {
        var friendId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        doNothing().when(commonService).checkUserExisting(any(UUID.class));

        when(blackListRepository.findByTargetUserIdAndAddingUserId(userId, friendId)).thenReturn(Optional.empty());
        var result = blackListService.getNoteData(userId, friendId);

        assertNotNull(result);
    }

    @Test(expected = ConflictException.class)
    public void testGetNoteData_ThrowsConflictException() {
        var friendId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        doNothing().when(commonService).checkUserExisting(any(UUID.class));

        when(blackListRepository.findByTargetUserIdAndAddingUserId(userId, friendId)).thenReturn(
                Optional.of(BlockedUserEntity.builder().deleteNoteDate(new Date()).build())
        );
        var result = blackListService.getNoteData(userId, friendId);

        assertNotNull(result);
    }

    @Test
    public void testAddNote() {
        var externalUserId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var userDto = UserDto.builder()
                .email("email")
                .login("login")
                .fullName("fullname")
                .build();

        doNothing().when(commonService).checkUserExisting(any(UUID.class));

        when(commonService.getUserById(any(UUID.class))).thenReturn(userDto);

        blackListService.addNote(externalUserId, userId);
    }

    @Test(expected = BadRequestException.class)
    public void testAddNote_ThrowsBadRequestException() {
        var externalUserId = UUID.randomUUID();
        var userId = externalUserId;
        var userDto = UserDto.builder()
                .email("email")
                .login("login")
                .fullName("fullname")
                .build();

        doNothing().when(commonService).checkUserExisting(any(UUID.class));

        when(commonService.getUserById(any(UUID.class))).thenReturn(userDto);

        blackListService.addNote(externalUserId, userId);
    }

    @Test
    public void testDeleteNote() {
        var externalUserId = UUID.randomUUID();
        var userId = UUID.randomUUID();;
        doNothing().when(commonService).checkUserExisting(any(UUID.class));
        when(blackListRepository.findByTargetUserIdAndAddingUserId(any(UUID.class), any(UUID.class))).thenReturn(
                Optional.of(new BlockedUserEntity())
        );

        blackListService.deleteNote(externalUserId, userId);
    }

    @Test(expected = BadRequestException.class)
    public void testDeleteNote_ThrowsBadRequestException() {
        var externalUserId = UUID.randomUUID();
        var userId = externalUserId;
        doNothing().when(commonService).checkUserExisting(any(UUID.class));
        when(blackListRepository.findByTargetUserIdAndAddingUserId(any(UUID.class), any(UUID.class))).thenReturn(
                Optional.of(new BlockedUserEntity())
        );

        blackListService.deleteNote(externalUserId, userId);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNote_ThrowsNotFoundException() {
        var externalUserId = UUID.randomUUID();
        var userId = UUID.randomUUID();;
        doNothing().when(commonService).checkUserExisting(any(UUID.class));
        when(blackListRepository.findByTargetUserIdAndAddingUserId(any(UUID.class), any(UUID.class))).thenReturn(
                Optional.empty()
        );

        blackListService.deleteNote(externalUserId, userId);
    }

    @Test(expected = ConflictException.class)
    public void testDeleteNote_ThrowsConflictException() {
        var externalUserId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        doNothing().when(commonService).checkUserExisting(any(UUID.class));
        when(blackListRepository.findByTargetUserIdAndAddingUserId(any(UUID.class), any(UUID.class))).thenReturn(
                Optional.of(BlockedUserEntity.builder().deleteNoteDate(new Date()).build())
        );

        blackListService.deleteNote(externalUserId, userId);
    }

    @Test
    public void testGetNotes() {
        InputPageDto inputPageDto = new InputPageDto(1, 10);
        PageFiltersDto pageFiltersDto = new PageFiltersDto();
        SortingDto sortingDto = new SortingDto(inputPageDto, pageFiltersDto);

        List<BlockedUserEntity> blockedList = Collections.singletonList(new BlockedUserEntity());
        Pageable pageable = PageRequest.of(inputPageDto.getPageNumber() - 1, inputPageDto.getPageSize());
        Page<BlockedUserEntity> page = new PageImpl<>(blockedList, pageable, blockedList.size());

        when(blackListRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(page);

        when(blackListRepository.findByFullNameLikeAndTargetUserId(any(String.class), any(UUID.class))).thenReturn(blockedList);

        var result = blackListService.getNotes(sortingDto, UUID.randomUUID());

        assertNotNull(result);
        assertEquals(blockedList.size(), result.getNotes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNotes_ThrowsIllegalArgumentException() {
        InputPageDto inputPageDto = new InputPageDto(0, 10);
        PageFiltersDto pageFiltersDto = new PageFiltersDto();
        SortingDto sortingDto = new SortingDto(inputPageDto, pageFiltersDto);

        List<BlockedUserEntity> blockedList = Collections.singletonList(new BlockedUserEntity());
        Pageable pageable = PageRequest.of(inputPageDto.getPageNumber() - 1, inputPageDto.getPageSize());
        Page<BlockedUserEntity> page = new PageImpl<>(blockedList, pageable, blockedList.size());

        when(blackListRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(page);

        when(blackListRepository.findByFullNameLikeAndTargetUserId(any(String.class), any(UUID.class))).thenReturn(blockedList);
        blackListService.getNotes(sortingDto, UUID.randomUUID());
    }

    @Test
    public void testSearchNotes() {
        InputPageDto inputPageDto = new InputPageDto(1, 10);
        SearchDto searchDto = new SearchDto(inputPageDto, "");

        List<BlockedUserEntity> blockedList = Collections.singletonList(new BlockedUserEntity());
        Pageable pageable = PageRequest.of(inputPageDto.getPageNumber() - 1, inputPageDto.getPageSize());
        Page<BlockedUserEntity> page = new PageImpl<>(blockedList, pageable, blockedList.size());

        when(blackListRepository.findAllByTargetUserId(any(UUID.class), any(Pageable.class))).thenReturn(page);

        when(blackListRepository.findByFullNameLikeAndTargetUserId(any(String.class), any(UUID.class))).thenReturn(blockedList);

        var result = blackListService.searchNotes(searchDto, UUID.randomUUID());

        assertNotNull(result);
        assertEquals(blockedList.size(), result.getNotes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchNotes_ThrowsIllegalArgumentException() {
        InputPageDto inputPageDto = new InputPageDto(0, 10);
        SearchDto searchDto = new SearchDto(inputPageDto, "");

        List<BlockedUserEntity> blockedList = Collections.singletonList(new BlockedUserEntity());
        Pageable pageable = PageRequest.of(inputPageDto.getPageNumber() - 1, inputPageDto.getPageSize());
        Page<BlockedUserEntity> page = new PageImpl<>(blockedList, pageable, blockedList.size());

        when(blackListRepository.findAllByTargetUserId(any(UUID.class), any(Pageable.class))).thenReturn(page);

        when(blackListRepository.findByFullNameLikeAndTargetUserId(any(String.class), any(UUID.class))).thenReturn(blockedList);

        var result = blackListService.searchNotes(searchDto, UUID.randomUUID());

        assertNotNull(result);
        assertEquals(blockedList.size(), result.getNotes().size());
    }

}
