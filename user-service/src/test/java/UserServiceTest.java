import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.security.props.SecurityIntegrationsProps;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import com.ithirteeng.messengerapi.common.service.CheckPaginationDetailsService;
import com.ithirteeng.messengerapi.user.dto.*;
import com.ithirteeng.messengerapi.user.entity.UserEntity;
import com.ithirteeng.messengerapi.user.mapper.UserMapper;
import com.ithirteeng.messengerapi.user.repository.UserRepository;
import com.ithirteeng.messengerapi.user.service.UserService;
import com.ithirteeng.messengerapi.user.utils.helper.PasswordHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private SecurityProps securityProps;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private CheckPaginationDetailsService checkPaginationDetailsService;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindUserEntityById() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(new UserEntity()));
        var result = userService.findUserEntityById(UUID.randomUUID());

        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void testFindUserEntityById_ThrowsNotFoundException() {
        userService.findUserEntityById(UUID.randomUUID());
    }

    @Test
    public void testGetUserByLogin() {
        when(repository.findByLogin(any(String.class))).thenReturn(Optional.of(new UserEntity()));
        var result = userService.getUserByLogin(UUID.randomUUID().toString());

        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetUserByLogin_ThrowsNotFoundException() {
        userService.getUserByLogin(UUID.randomUUID().toString());
    }

    @Test
    public void testPostLogin() {
        UserEntity userEntity = UserEntity.builder()
                .password("test_p")
                .login("test_login")
                .email("test@email.com")
                .id(UUID.randomUUID())
                .fullName("TEST FULLNAME")
                .build();
        try {
            mockStatic(PasswordHelper.class);
        } catch (Exception e) {
        }
        try {
            mockStatic(UserMapper.class);
        } catch (Exception e) {}
        when(repository.findByLogin(any(String.class))).thenReturn(Optional.of(userEntity));
        when(PasswordHelper.isPasswordValid(any(String.class), any(String.class))).thenReturn(true);
        when(UserMapper.entityToUserDto(userEntity)).thenReturn(new UserDto());
        var result = userService.postLogin(new LoginDto("SFD", "SDFSD"));

        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void testPostLogin_ThrowsNotFoundException() {
        try {
            mockStatic(PasswordHelper.class);
        } catch (Exception e) {
        }
        when(PasswordHelper.isPasswordValid(ArgumentMatchers.any(String.class), ArgumentMatchers.any(String.class))).thenReturn(true);
        when(bCryptPasswordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        var result = userService.postLogin(new LoginDto("SFD", "SDFSD"));

        assertNotNull(result);
    }

    @Test(expected = BadRequestException.class)
    public void testPostLogin_ThrowsBadRequestException() {
        UserEntity userEntity = UserEntity.builder()
                .password("test_p")
                .login("test_login")
                .email("test@email.com")
                .id(UUID.randomUUID())
                .fullName("TEST FULLNAME")
                .build();

        try {
            mockStatic(PasswordHelper.class);
        } catch (Exception e) {
        }

        when(repository.findByLogin(any(String.class))).thenReturn(Optional.of(userEntity));
        when(PasswordHelper.isPasswordValid(any(String.class), any(String.class))).thenReturn(false);
        when(bCryptPasswordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        var result = userService.postLogin(new LoginDto("SFD", "SDFSD"));

        assertNotNull(result);
    }

    @Test
    public void testRegistration() {
        UserEntity userEntity = UserEntity.builder()
                .password("test_p")
                .login("test_login")
                .email("test@email.com")
                .id(UUID.randomUUID())
                .fullName("TEST FULLNAME")
                .build();

        var registration = RegistrationDto.builder()
                .password("test_password")
                .login("test_login")
                .email("test@email.test")
                .build();

        try {
            mockStatic(UserMapper.class);
        } catch (Exception e) {
        }

        when(repository.existsByLoginOrEmail(any(String.class), any(String.class))).thenReturn(false);
        when(repository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(UserMapper.registrationDtoToUserEntity(registration)).thenReturn(userEntity);
        when(UserMapper.entityToUserDto(userEntity)).thenReturn(UserDto.builder().fullName("SDFsdfs").build());

        var result = userService.postRegistration(registration);

        assertNotNull(result);
    }

    @Test(expected = ConflictException.class)
    public void testRegistration_ThrowsConflictException() {
        UserEntity userEntity = UserEntity.builder()
                .password("test_p")
                .login("test_login")
                .email("test@email.com")
                .id(UUID.randomUUID())
                .fullName("TEST FULLNAME")
                .build();

        var registration = RegistrationDto.builder()
                .password("test_password")
                .login("test_login")
                .email("test@email.test")
                .build();

        try {
            mockStatic(UserMapper.class);
        } catch (Exception e) {
        }

        when(repository.existsByLoginOrEmail(any(String.class), any(String.class))).thenReturn(true);
        when(repository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(UserMapper.registrationDtoToUserEntity(registration)).thenReturn(userEntity);
        when(UserMapper.entityToUserDto(userEntity)).thenReturn(UserDto.builder().fullName("SDFsdfs").build());

        var result = userService.postRegistration(registration);

        assertNotNull(result);
    }

    @Test
    public void testUpdateProfile() {
        UserEntity userEntity = UserEntity.builder()
                .password("test_p")
                .login("test_login")
                .email("test@email.com")
                .id(UUID.randomUUID())
                .fullName("TEST FULLNAME")
                .build();

        try {
            mockStatic(UserMapper.class);
        } catch (Exception e) {
        }

        var updateProfileDto = UpdateProfileDto.builder().fullName("TEST").build();

        when(repository.findByLogin(any(String.class))).thenReturn(Optional.of(userEntity));
        when(UserMapper.updateUserFields(userEntity, updateProfileDto)).thenReturn(userEntity);
        when(UserMapper.entityToUserDto(userEntity)).thenReturn(UserDto.builder().fullName("SDFsdfs").build());

        var result = userService.updateProfile(updateProfileDto, "login");

        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateProfile_ThrowsNotFoundException() {
        UserEntity userEntity = UserEntity.builder()
                .password("test_p")
                .login("test_login")
                .email("test@email.com")
                .id(UUID.randomUUID())
                .fullName("TEST FULLNAME")
                .build();

        try {
            mockStatic(UserMapper.class);
        } catch (Exception e) {
        }

        var updateProfileDto = UpdateProfileDto.builder().fullName("TEST").build();

        when(repository.findByLogin(any(String.class))).thenReturn(Optional.empty());
        when(UserMapper.updateUserFields(userEntity, updateProfileDto)).thenReturn(userEntity);
        when(UserMapper.entityToUserDto(userEntity)).thenReturn(UserDto.builder().fullName("SDFsdfs").build());

        var result = userService.updateProfile(updateProfileDto, "login");

        assertNotNull(result);
    }

    @Test
    public void testGetUserData_HttpClientErrorException() {
        var userId = UUID.randomUUID();
        var friendId = UUID.randomUUID();
        UserEntity userEntity = UserEntity.builder()
                .password("test_p")
                .login("test_login")
                .email("test@email.com")
                .id(userId)
                .fullName("TEST FULLNAME")
                .build();


        SecurityIntegrationsProps securityIntegrationsProps = new SecurityIntegrationsProps();
        securityIntegrationsProps.setRootPath("sdfdsfsd");
        securityIntegrationsProps.setApiKey("SFSDFSDFSDFSDFSDFSDFSDFSDFSD");

        when(repository.findByLogin(any(String.class))).thenReturn(Optional.of(userEntity));
        when(securityProps.getIntegrations()).thenReturn(securityIntegrationsProps);
        assertThrows(Exception.class, () -> userService.getUserData("login", friendId));
    }

    @Test
    public void testGetUsersList() {
        InputPageDto inputPageDto = new InputPageDto(1, 10);
        SortingFieldsDto sortingFieldsDto = new SortingFieldsDto();
        SortingFiltersDto sortingFiltersDto = new SortingFiltersDto();
        sortingFiltersDto.setLogin("test");
        sortingFiltersDto.setEmail("test@test.test");

        SortingDto sortingDto = new SortingDto(inputPageDto, sortingFiltersDto, sortingFieldsDto);
        List<UserEntity> usersList = Collections.singletonList(new UserEntity());
        Page<UserEntity> usersPage = new PageImpl<>(usersList);

        when(repository.findAll(any(Example.class), any(Pageable.class))).thenReturn(usersPage);

        Page<UserDto> resultPage = userService.getUsersList(sortingDto);

        assertEquals(usersList.size(), resultPage.getContent().size());
    }
}
