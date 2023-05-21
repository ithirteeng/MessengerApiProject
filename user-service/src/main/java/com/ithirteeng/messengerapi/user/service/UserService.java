package com.ithirteeng.messengerapi.user.service;

import com.ithirteeng.messengerapi.common.consts.RequestsConstants;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import com.ithirteeng.messengerapi.common.service.CheckPaginationDetailsService;
import com.ithirteeng.messengerapi.common.service.EnablePaginationService;
import com.ithirteeng.messengerapi.user.dto.*;
import com.ithirteeng.messengerapi.user.entity.UserEntity;
import com.ithirteeng.messengerapi.user.mapper.UserMapper;
import com.ithirteeng.messengerapi.user.repository.UserRepository;
import com.ithirteeng.messengerapi.user.utils.helper.PasswordHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Сервис для работы с данными о юзере
 */
@Service
@EnablePaginationService
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final CheckPaginationDetailsService paginationDetailsService;

    private final SecurityProps securityProps;

    private final StreamBridge streamBridge;

    /**
     * Метод для получения данных о пользователе по id
     *
     * @param id id пользователя в формате {@link UUID}
     * @return объект типа {@link UserEntity}
     * @throws NotFoundException возникает при несуществующем id пользователя
     */
    @Transactional(readOnly = true)
    public UserEntity findUserEntityById(UUID id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователся с таким id " + id + " не существует"));
    }

    /**
     * Метод для получения данных о пользователе по логину
     *
     * @param login логин
     * @return объект типа {@link UserEntity}
     * @throws NotFoundException возникает при несуществующем логине
     */
    @Transactional(readOnly = true)
    public UserEntity getUserByLogin(String login) {
        return repository
                .findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким логином не существует!"));
    }

    /**
     * Метод для аутентификации по логину
     *
     * @param loginDto DTO с данными для логина
     * @return объект типа {@link UserDto}
     * @throws BadRequestException возникает при неверном пароле
     * @throws NotFoundException   возникает при несущесвующем логине
     */
    @Transactional(readOnly = true)
    public UserDto postLogin(LoginDto loginDto) {
        var entity = getUserByLogin(loginDto.getLogin());
        if (!PasswordHelper.isPasswordValid(loginDto.getPassword(), entity.getPassword())) {
            throw new BadRequestException("Неверный пароль");
        } else {
            return UserMapper.entityToUserDto(entity);
        }
    }

    /**
     * Метод для регистрации пользователя
     *
     * @param registrationDto DTO с данными для регистрации
     * @return объект типа {@link UserDto}
     * @throws ConflictException возникает при уже существующих email или login
     */
    @Transactional
    public UserDto postRegistration(RegistrationDto registrationDto) {
        if (repository.existsByLoginOrEmail(registrationDto.getLogin(), registrationDto.getEmail())) {
            throw new ConflictException("Такой пользователь уже существует!");
        } else {
            var entity = repository.save(UserMapper.registrationDtoToUserEntity(registrationDto));
            return UserMapper.entityToUserDto(entity);
        }

    }

    /**
     * Метод для изменения профиля пользователя
     *
     * @param updateProfileDto DTO с данными для Изменения профиля
     * @return объект типа {@link UserDto}
     * @throws NotFoundException возникает при несуществующем пользователе
     */
    @Transactional
    public UserDto updateProfile(UpdateProfileDto updateProfileDto, String login) {
        var entity = repository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Такого пользователя не существует"));
        entity = UserMapper.updateUserFields(entity, updateProfileDto);
        repository.save(entity);
        syncUserData(entity.getId());
        return UserMapper.entityToUserDto(entity);
    }

    /**
     * Метод для синхронизации имени пользователя через {@link StreamBridge}
     *
     * @param userId идентификатор пользователя
     */
    private void syncUserData(UUID userId) {
        streamBridge.send("userDataSyncEvent-out-0", userId);
    }

    /**
     * Метод для получения данных по объекту класса {@link SortingDto} c пагинацией
     *
     * @param sortingDto объект класса {@link SortingDto}
     * @return {@link Page}<{@link UserDto}>
     * @throws BadRequestException при номере страницы не должен превышать общее число онных - 1
     */
    @Transactional
    public Page<UserDto> getUsersList(SortingDto sortingDto) {
        var pageInfo = sortingDto.getPageInfo();
        paginationDetailsService.checkPagination(pageInfo.getPageNumber(), pageInfo.getPageSize());
        var filtersInfo = sortingDto.getFilters();
        UserEntity exampleUser = UserEntity.from(
                filtersInfo.getFullName(),
                filtersInfo.getLogin(),
                filtersInfo.getEmail(),
                filtersInfo.getCity(),
                filtersInfo.getBirthDate(),
                filtersInfo.getTelephoneNumber()
        );
        Sort sort = Sort.by(setupSortData(sortingDto.getFields()));
        Example<UserEntity> example = Example.of(exampleUser);

        Pageable pageable = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize(), sort);
        Page<UserEntity> users = repository.findAll(example, pageable);

        if (users.getTotalPages() <= pageInfo.getPageNumber() && users.getTotalPages() != 0) {
            throw new BadRequestException("Номер страницы не должен превышать общее число онных - 1");
        }
        return users.map(UserMapper::entityToUserDto);
    }

    /**
     * Метод для получения списков объектов типа {@link Sort.Order}, чтобы отсортировать наш список по нужным полям
     *
     * @param sortingFieldsDto - ДТО полей, которые нужно отсортировать
     * @return {@link List}<{@link Sort.Order}>
     */
    private List<Sort.Order> setupSortData(SortingFieldsDto sortingFieldsDto) {
        List<Sort.Order> list = new ArrayList<>();
        list.add(getOrder(sortingFieldsDto.getLogin(), "login"));
        list.add(getOrder(sortingFieldsDto.getEmail(), "email"));
        list.add(getOrder(sortingFieldsDto.getFullName(), "fullName"));
        list.add(getOrder(sortingFieldsDto.getBirthDate(), "birthDate"));
        list.add(getOrder(sortingFieldsDto.getTelephoneNumber(), "telephoneNumber"));
        list.add(getOrder(sortingFieldsDto.getCity(), "city"));
        list.removeIf(Objects::isNull);
        return list;
    }

    /**
     * Методя для получения корректного объекта типа {@link Sort.Order} или null по критериям ниже
     *
     * @param direction объект типа {@link org.springframework.data.domain.Sort.Direction}
     * @param field     название поля таблицы, по которому сортировка ({@link String})
     * @return null или {@link Sort.Order}
     */
    private Sort.Order getOrder(Sort.Direction direction, String field) {
        if (direction == null) {
            return null;
        } else {
            return new Sort.Order(direction, field);
        }
    }

    /**
     * Метод для получения пользователя по логину (с проверкой по черному списку)
     *
     * @param login        логин внешнего пользователя
     * @param targetUserId Id целевого пользователя
     * @return {@link UserDto}
     */
    public UserDto getUserData(String login, UUID targetUserId) {
        var entity = repository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Такого пользоателя не существует"));
        if (checkIfUserInBlackList(targetUserId, entity.getId())) {
            throw new BadRequestException("Пользователь добавил вас в черный список");
        }
        return UserMapper.entityToUserDto(entity);
    }

    /**
     * Метод проверки нахождения целевого пользовтеля в черном списке внешнего пользователя
     *
     * @param checkUserId  Id внешнего пользователя
     * @param targetUserId Id целевого пользователя
     * @return {@link Boolean}
     */
    private Boolean checkIfUserInBlackList(UUID checkUserId, UUID targetUserId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:1308/integration/blacklist/" + checkUserId.toString() + "/" + targetUserId.toString();

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, setupRequestHttpEntity(), Boolean.class
        );

        return responseEntity.getBody();
    }

    /**
     * Вспомогательный метод для сборки {@link HttpEntity}<{@link Void}>
     *
     * @return {@link HttpEntity}<{@link Void}>
     */
    private HttpEntity<Void> setupRequestHttpEntity() {
        HttpHeaders headers = new HttpHeaders();

        headers.set(RequestsConstants.API_KEY_HEADER, securityProps.getIntegrations().getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }


}
