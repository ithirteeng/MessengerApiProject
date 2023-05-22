package com.ithirteeng.messengerapi.friends.controller;

import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.friends.dto.blacklist.AddDeleteNoteDto;
import com.ithirteeng.messengerapi.friends.dto.blacklist.FullNoteDto;
import com.ithirteeng.messengerapi.friends.dto.blacklist.OutputNotesPageDto;
import com.ithirteeng.messengerapi.friends.dto.common.SearchDto;
import com.ithirteeng.messengerapi.friends.dto.common.SortingDto;
import com.ithirteeng.messengerapi.friends.service.BlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Контроллер для запросов сервиса черного списка
 */
@RestController
@RequestMapping("/api/blacklist")
@RequiredArgsConstructor
public class BlackListController {

    private final BlackListService blackListService;

    /**
     * Метод для добавления пользователя в ЧС
     *
     * @param addDeleteNoteDto ДТО ({@link AddDeleteNoteDto}) для добавления в ЧС
     * @param authentication {@link Authentication}
     */
    @PostMapping("/add")
    public void addNote(@Validated @RequestBody AddDeleteNoteDto addDeleteNoteDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        blackListService.addNote(addDeleteNoteDto.getExternalUserId(), userData.getId());
    }

    /**
     * Метод для получения данных о заблокированном пользователе
     *
     * @param externalUserId идентификатор внешнего пользователя
     * @param authentication {@link Authentication}
     * @return {@link FullNoteDto}
     */
    @GetMapping("{id}")
    public FullNoteDto getBlockedUserData(@PathVariable("id") UUID externalUserId, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return blackListService.getNoteData(userData.getId(), externalUserId);
    }

    /**
     * Метод для удаления из ЧС
     *
     * @param addDeleteNoteDto ДТО ({@link AddDeleteNoteDto}) для удаления из ЧС
     * @param authentication {@link Authentication}
     */
    @DeleteMapping("/delete")
    public void deleteNote(@Validated @RequestBody AddDeleteNoteDto addDeleteNoteDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        blackListService.deleteNote(addDeleteNoteDto.getExternalUserId(), userData.getId());
    }

    /**
     * Метод для получения данных в виде сортированного по параметрам списка с пагинацией
     *
     * @param sortingDto ДТО ({@link SortingDto}) с параметрами сортировки и пагинации
     * @param authentication {@link Authentication}
     * @return {@link OutputNotesPageDto}
     */
    @PostMapping("/list")
    public OutputNotesPageDto getList(@Valid @RequestBody SortingDto sortingDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return blackListService.getNotes(sortingDto, userData.getId());
    }

    /**
     * Метод для поиска по ЧС и вывода с пагинацией
     *
     * @param searchDto ДТО ({@link SearchDto}) с параметрами пагинации и поиска
     * @param authentication {@link Authentication}
     * @return {@link OutputNotesPageDto}
     */
    @PostMapping("/search")
    public OutputNotesPageDto searchNotes(@Valid @RequestBody SearchDto searchDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return blackListService.searchNotes(searchDto, userData.getId());
    }

    /**
     * Метод для обновления ФИО пользователя из ЧС
     *
     * @param id идентификатор юзера
     * @param authentication {@link Authentication}
     */
    @PatchMapping(value = "/{id}/update")
    public void updateFriendsFullName(@PathVariable("id") UUID id, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        blackListService.updateFullNameFields(id);
    }

    /**
     *  Метод для проверки нахождения юзера в ЧС
     *
     * @param id идентификатор пользователя
     * @param authentication {@link Authentication}
     * @return {@link Boolean}
     */
    @GetMapping(value = "/check/{id}")
    public Boolean checkUserExisting(@PathVariable("id") UUID id, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return blackListService.checkIfUserInBlackList(userData.getId(), id);
    }

}
