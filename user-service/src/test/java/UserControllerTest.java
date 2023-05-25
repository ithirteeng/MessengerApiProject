import com.fasterxml.jackson.databind.ObjectMapper;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.security.EnableSpringSecurity;
import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.common.security.utils.SecurityUtil;
import com.ithirteeng.messengerapi.user.dto.GetProfileDto;
import com.ithirteeng.messengerapi.user.dto.LoginDto;
import com.ithirteeng.messengerapi.user.dto.RegistrationDto;
import com.ithirteeng.messengerapi.user.entity.UserEntity;
import com.ithirteeng.messengerapi.user.mapper.UserMapper;
import com.ithirteeng.messengerapi.user.service.AuthenticationService;
import com.ithirteeng.messengerapi.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@EnableSpringSecurity
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.ithirteeng.messengerapi.user.UserServerApplication.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

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
    public void testGetMineData() throws Exception {
        var userDto = UserDto.builder()
                .id(UUID.randomUUID())
                .fullName("TEST")
                .login("test")
                .email("test@test.test")
                .build();

        JwtUserDetails jwtUserDetails = new JwtUserDetails(
                UUID.randomUUID(),
                "login",
                "fullname"
        );

        try {
            mockStatic(SecurityUtil.class);
        } catch (Exception e){}
        try {
            mockStatic(UserMapper.class);
        } catch (Exception e){}

        when(SecurityUtil.getUserData()).thenReturn(jwtUserDetails);
        when(userService.findUserEntityById(any(UUID.class))).thenReturn(new UserEntity());
        when(UserMapper.entityToUserDto(any(UserEntity.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                        .accept(MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.fullName").value(userDto.getFullName()));
    }

    @Test
    public void testGetProfileData() throws Exception {
        var userDto = UserDto.builder()
                .id(UUID.randomUUID())
                .fullName("TEST")
                .login("test")
                .email("test@test.test")
                .build();

        JwtUserDetails jwtUserDetails = new JwtUserDetails(
                UUID.randomUUID(),
                "login",
                "fullname"
        );

        try {
            mockStatic(SecurityUtil.class);
        } catch (Exception e){}

        GetProfileDto getProfileDto = new GetProfileDto("test");
        when(SecurityUtil.getUserData()).thenReturn(jwtUserDetails);
        when(userService.getUserData("test", jwtUserDetails.getId())).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/profile")
                        .accept(MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(getProfileDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.fullName").value(userDto.getFullName()));


    }



}
