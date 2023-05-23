import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.security.props.SecurityJwtTokenProps;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import com.ithirteeng.messengerapi.user.dto.LoginDto;
import com.ithirteeng.messengerapi.user.dto.RegistrationDto;
import com.ithirteeng.messengerapi.user.entity.UserEntity;
import com.ithirteeng.messengerapi.user.repository.UserRepository;
import com.ithirteeng.messengerapi.user.service.AuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityProps securityProps;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void checkGenerateJwtForLogin() {
        LoginDto loginDto = new LoginDto(
                "test_login",
                "test_password"
        );

        UserEntity userEntity = UserEntity.builder()
                .password("test_p")
                .login("test_login")
                .email("test@email.com")
                .id(UUID.randomUUID())
                .fullName("TEST FULLNAME")
                .build();

        SecurityJwtTokenProps securityJwtTokenProps = new SecurityJwtTokenProps();

        securityJwtTokenProps.setExpiration(1L);
        securityJwtTokenProps.setSecret("SDSDFGSDFGSDFGSDFGSDFGSDFGSDFGSDFGSDFGSDFGSDFGSDFGF");
        securityJwtTokenProps.setRootPath("SDFfs");
        securityJwtTokenProps.setPermitAll(new String[2]);

        when(userRepository.findByLogin(loginDto.getLogin())).thenReturn(Optional.of(userEntity));
        when(bCryptPasswordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(securityProps.getJwtToken()).thenReturn(securityJwtTokenProps);

        var result = authenticationService.generateJwt(loginDto);

        assertNotNull(result);
    }

    @Test(expected = BadRequestException.class)
    public void checkGenerateJwtForLogin_ThrowsBadRequestException() {
        LoginDto loginDto = new LoginDto(
                "test_login",
                "test_password"
        );

        UserEntity userEntity = UserEntity.builder()
                .password("test_p")
                .login("test_login")
                .email("test@email.com")
                .id(UUID.randomUUID())
                .fullName("TEST FULLNAME")
                .build();

        when(userRepository.findByLogin(loginDto.getLogin())).thenReturn(Optional.of(userEntity));
        authenticationService.generateJwt(loginDto);
    }

    @Test(expected = NotFoundException.class)
    public void checkGenerateJwtForLogin_ThrowsNotFoundException() {
        LoginDto loginDto = new LoginDto(
                "test_login",
                "test_password"
        );
        authenticationService.generateJwt(loginDto);
    }
    @Test
    public void checkGenerateJwtForRegistration() {
        RegistrationDto registrationDto = RegistrationDto.builder()
                .password("test_password")
                .login("test_login")
                .email("test@email.test")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .password("test_p")
                .login("test_login")
                .email("test@email.com")
                .id(UUID.randomUUID())
                .fullName("TEST FULLNAME")
                .build();

        SecurityJwtTokenProps securityJwtTokenProps = new SecurityJwtTokenProps();

        securityJwtTokenProps.setExpiration(1L);
        securityJwtTokenProps.setSecret("SDSDFGSDFGSDFGSDFGSDFGSDFGSDFGSDFGSDFGSDFGSDFGSDFGF");
        securityJwtTokenProps.setRootPath("SDFfs");
        securityJwtTokenProps.setPermitAll(new String[2]);

        when(userRepository.findByLogin(registrationDto.getLogin())).thenReturn(Optional.of(userEntity));
        when(securityProps.getJwtToken()).thenReturn(securityJwtTokenProps);
        var result = authenticationService.generateJwt(registrationDto);

        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void checkGenerateJwtForRegistration_ThrowsNotFoundException() {
        RegistrationDto registrationDto = RegistrationDto.builder()
                .password("test_password")
                .login("test_login")
                .email("test@email.test")
                .build();

       authenticationService.generateJwt(registrationDto);
    }
}
