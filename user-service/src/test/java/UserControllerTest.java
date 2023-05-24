import com.fasterxml.jackson.databind.ObjectMapper;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.security.jwt.JwtAuthentication;
import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.user.dto.GetProfileDto;
import com.ithirteeng.messengerapi.user.dto.LoginDto;
import com.ithirteeng.messengerapi.user.dto.RegistrationDto;
import com.ithirteeng.messengerapi.user.entity.UserEntity;
import com.ithirteeng.messengerapi.user.service.AuthenticationService;
import com.ithirteeng.messengerapi.user.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.ithirteeng.messengerapi.user.UserServerApplication.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private StreamBridge streamBridge;

    @Test
    public void testRegisterUser() throws Exception {
        var registration = RegistrationDto.builder()
                .password("test_p")
                .login("test_login")
                .email("test@email.test")
                .fullName("Ivan Ivan")
                .build();
        var userDto = UserDto.builder()
                .id(UUID.randomUUID())
                .fullName(registration.getFullName())
                .login(registration.getLogin())
                .email(registration.getEmail())
                .build();
        when(userService.postRegistration(registration)).thenReturn(userDto);
        when(authenticationService.generateJwt(registration)).thenReturn("token");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/registration")
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.fullName").value(userDto.getFullName()))
                .andExpect(header().string("Authorization", "token"));


    }

    @Test
    public void testLoginUser() throws Exception {
        var loginDto = new LoginDto("test", "test");
        var userDto = UserDto.builder()
                .id(UUID.randomUUID())
                .fullName("TEST")
                .login(loginDto.getLogin())
                .email("test@test.test")
                .build();

        when(userService.postLogin(loginDto)).thenReturn(userDto);
        when(authenticationService.generateJwt(loginDto)).thenReturn("token");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.fullName").value(userDto.getFullName()))
                .andExpect(header().string("Authorization", "token"));
    }

    @Test
    public void testGetProfileData() throws Exception {
        var userData = new JwtUserDetails(UUID.randomUUID(), "test", "test");
        var userDto = UserDto.builder()
                .id(UUID.randomUUID())
                .fullName("TEST")
                .login("test")
                .email("test@test.test")
                .build();

        var authentication = new JwtAuthentication(userData);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //when(userService.getUserData("test", userData.getId())).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                        .with(securityContext(SecurityContextHolder.getContext()))
                        .accept(MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON))
                     //   .content("{ \"login\": \"test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.fullName").value(userDto.getFullName()));
    }

}
