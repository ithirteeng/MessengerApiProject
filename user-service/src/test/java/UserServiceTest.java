import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.security.props.SecurityIntegrationsProps;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import com.ithirteeng.messengerapi.common.service.CheckPaginationDetailsService;
import com.ithirteeng.messengerapi.user.dto.LoginDto;
import com.ithirteeng.messengerapi.user.dto.RegistrationDto;
import com.ithirteeng.messengerapi.user.dto.UpdateProfileDto;
import com.ithirteeng.messengerapi.user.entity.UserEntity;
import com.ithirteeng.messengerapi.user.mapper.UserMapper;
import com.ithirteeng.messengerapi.user.repository.UserRepository;
import com.ithirteeng.messengerapi.user.service.CommonService;
import com.ithirteeng.messengerapi.user.service.UserService;
import com.ithirteeng.messengerapi.user.utils.helper.PasswordHelper;
import org.apache.http.protocol.HTTP;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private CheckPaginationDetailsService paginationDetailsService;

    @Mock
    private SecurityProps securityProps;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private CommonService commonService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private RestTemplate restTemplate;

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

        mockStatic(PasswordHelper.class);

        when(repository.findByLogin(any(String.class))).thenReturn(Optional.of(userEntity));
        when(PasswordHelper.isPasswordValid(any(String.class), any(String.class))).thenReturn(true);
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

        mockStatic(UserMapper.class);

        when(repository.existsByLoginOrEmail(any(String.class), any(String.class))).thenReturn(false);
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

        var updateProfileDto = UpdateProfileDto.builder().fullName("TEST").build();

        when(repository.findByLogin(any(String.class))).thenReturn(Optional.of(userEntity));
        when(UserMapper.updateUserFields(userEntity, updateProfileDto)).thenReturn(userEntity);
        when(UserMapper.entityToUserDto(userEntity)).thenReturn(UserDto.builder().fullName("SDFsdfs").build());

        var result = userService.updateProfile(updateProfileDto, "login");

        assertNotNull(result);
    }

    @Test
    public void testGetUserData_ThrowsResourceAccessException() {
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
        assertThrows(ResourceAccessException.class, () -> userService.getUserData("login", friendId));
    }
}
