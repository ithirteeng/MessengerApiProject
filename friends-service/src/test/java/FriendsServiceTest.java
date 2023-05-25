import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.service.CheckPaginationDetailsService;
import com.ithirteeng.messengerapi.friends.dto.common.InputPageDto;
import com.ithirteeng.messengerapi.friends.dto.common.PageFiltersDto;
import com.ithirteeng.messengerapi.friends.dto.common.SearchDto;
import com.ithirteeng.messengerapi.friends.dto.common.SortingDto;
import com.ithirteeng.messengerapi.friends.entity.FriendEntity;
import com.ithirteeng.messengerapi.friends.repository.FriendsRepository;
import com.ithirteeng.messengerapi.friends.service.BlackListService;
import com.ithirteeng.messengerapi.friends.service.CommonService;
import com.ithirteeng.messengerapi.friends.service.FriendsService;
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
public class FriendsServiceTest {

    @Mock
    private FriendsRepository friendsRepository;

    @Mock
    private CommonService commonService;

    @Mock
    private CheckPaginationDetailsService paginationDetailsService;

    @Mock
    private BlackListService blackListService;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private FriendsService friendsService;

    @Test
    public void testGetFriendData() {
        var friendId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        doNothing().when(commonService).checkUserExisting(any(UUID.class));

        when(friendsRepository.findByTargetUserIdAndAddingUserId(userId, friendId)).thenReturn(Optional.of(new FriendEntity()));
        var result = friendsService.getFriendData(userId, friendId);

        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetFriendData_ThrowsNotFoundException() {
        var friendId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        doNothing().when(commonService).checkUserExisting(any(UUID.class));

        var result = friendsService.getFriendData(userId, friendId);

        assertNotNull(result);
    }

    @Test
    public void testAddFriend() {
        var friendId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var userDto = UserDto.builder()
                .email("email")
                .login("login")
                .fullName("fullname")
                .build();

        doNothing().when(commonService).checkUserExisting(any(UUID.class));

        when(blackListService.checkIfTargetUserInExternalUsersBlackList(any(UUID.class), any(UUID.class))).thenReturn(false);

        when(commonService.getUserById(any(UUID.class))).thenReturn(userDto);
        when(friendsRepository.findByTargetUserIdAndAddingUserId(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(new FriendEntity()));

        friendsService.addFriend(friendId, userId);

    }

    @Test(expected = ConflictException.class)
    public void testAddFriend_ThrowsConflictException() {
        var friendId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var userDto = UserDto.builder()
                .email("email")
                .login("login")
                .fullName("fullname")
                .build();

        doNothing().when(commonService).checkUserExisting(any(UUID.class));

        when(blackListService.checkIfTargetUserInExternalUsersBlackList(any(UUID.class), any(UUID.class))).thenReturn(true);

        when(commonService.getUserById(any(UUID.class))).thenReturn(userDto);
        when(friendsRepository.findByTargetUserIdAndAddingUserId(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(new FriendEntity()));

        friendsService.addFriend(friendId, userId);

    }

    @Test(expected = BadRequestException.class)
    public void testAddFriend_ThrowsBadRequestException() {
        var friendId = UUID.randomUUID();
        var userId = friendId;
        var userDto = UserDto.builder()
                .email("email")
                .login("login")
                .fullName("fullname")
                .build();

        doNothing().when(commonService).checkUserExisting(any(UUID.class));

        when(blackListService.checkIfTargetUserInExternalUsersBlackList(any(UUID.class), any(UUID.class))).thenReturn(false);

        when(commonService.getUserById(any(UUID.class))).thenReturn(userDto);
        when(friendsRepository.findByTargetUserIdAndAddingUserId(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(new FriendEntity()));

        friendsService.addFriend(friendId, userId);

    }

    @Test
    public void testDeleteFriend() {
        var friendId = UUID.randomUUID();
        var userId = UUID.randomUUID();;
        doNothing().when(commonService).checkUserExisting(any(UUID.class));
        when(friendsRepository.findByTargetUserIdAndAddingUserId(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(new FriendEntity()));

        friendsService.deleteFriend(friendId, userId);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteFriend_ThrowsNotFoundException() {
        var friendId = UUID.randomUUID();
        var userId = UUID.randomUUID();;
        doNothing().when(commonService).checkUserExisting(any(UUID.class));
        when(friendsRepository.findByTargetUserIdAndAddingUserId(any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());

        friendsService.deleteFriend(friendId, userId);
    }

    @Test(expected = ConflictException.class)
    public void testDeleteFriend_ThrowsConflictException() {
        var friendId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        doNothing().when(commonService).checkUserExisting(any(UUID.class));
        when(friendsRepository.findByTargetUserIdAndAddingUserId(any(UUID.class), any(UUID.class))).thenReturn(
                Optional.of(FriendEntity.builder().deleteFriendDate(new Date()).build())
        );

        friendsService.deleteFriend(friendId, userId);
    }

    @Test
    public void testGetFriendsPage() {
        InputPageDto inputPageDto = new InputPageDto(1, 10);
        PageFiltersDto pageFiltersDto = new PageFiltersDto();
        SortingDto sortingDto = new SortingDto(inputPageDto, pageFiltersDto);

        List<FriendEntity> friendsList = Collections.singletonList(new FriendEntity());
        Pageable pageable = PageRequest.of(inputPageDto.getPageNumber() - 1, inputPageDto.getPageSize());
        Page<FriendEntity> page = new PageImpl<>(friendsList, pageable, friendsList.size());



        when(friendsRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(page);

        when(friendsRepository.findByFullNameLikeAndTargetUserId(any(String.class), any(UUID.class))).thenReturn(friendsList);

        var result = friendsService.getFriendsPage(sortingDto, UUID.randomUUID());

        assertNotNull(result);
        assertEquals(friendsList.size(), result.getFriends().size());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFriendsPage_ThrowsIllegalArgumentException() {
        InputPageDto inputPageDto = new InputPageDto(0, 10);
        PageFiltersDto pageFiltersDto = new PageFiltersDto();
        SortingDto sortingDto = new SortingDto(inputPageDto, pageFiltersDto);
        List<FriendEntity> friendsList = Collections.singletonList(new FriendEntity());

        Pageable pageable = PageRequest.of(inputPageDto.getPageNumber() - 1, inputPageDto.getPageSize());
        Page<FriendEntity> page = new PageImpl<>(friendsList, pageable, friendsList.size());

        when(friendsRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(page);
        when(friendsRepository.findByFullNameLikeAndTargetUserId(any(String.class), any(UUID.class))).thenReturn(friendsList);

        page.getPageable().getPageNumber();
        friendsService.getFriendsPage(sortingDto, UUID.randomUUID());
    }

    @Test
    public void testSearchFriends() {
        InputPageDto inputPageDto = new InputPageDto(1, 10);
        SearchDto searchDto = new SearchDto(inputPageDto, "");

        List<FriendEntity> friendsList = Collections.singletonList(new FriendEntity());
        Pageable pageable = PageRequest.of(inputPageDto.getPageNumber() - 1, inputPageDto.getPageSize());
        Page<FriendEntity> page = new PageImpl<>(friendsList, pageable, friendsList.size());

        when(friendsRepository.findAllByTargetUserId(any(UUID.class), any(Pageable.class))).thenReturn(page);
        when(friendsRepository.findByFullNameLikeAndTargetUserId(any(String.class), any(UUID.class))).thenReturn(friendsList);

        var result = friendsService.searchFriends(searchDto, UUID.randomUUID());

        assertNotNull(result);
        assertEquals(friendsList.size(), result.getFriends().size());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchFriends_ThrowsIllegalArgumentException() {
        InputPageDto inputPageDto = new InputPageDto(0, 10);
        SearchDto searchDto = new SearchDto(inputPageDto, "");

        List<FriendEntity> friendsList = Collections.singletonList(new FriendEntity());
        Pageable pageable = PageRequest.of(inputPageDto.getPageNumber() - 1, inputPageDto.getPageSize());
        Page<FriendEntity> page = new PageImpl<>(friendsList, pageable, friendsList.size());

        when(friendsRepository.findAllByTargetUserId(any(UUID.class), any(Pageable.class))).thenReturn(page);
        when(friendsRepository.findByFullNameLikeAndTargetUserId(any(String.class), any(UUID.class))).thenReturn(friendsList);

        var result = friendsService.searchFriends(searchDto, UUID.randomUUID());

        assertNotNull(result);
        assertEquals(friendsList.size(), result.getFriends().size());

    }

}
