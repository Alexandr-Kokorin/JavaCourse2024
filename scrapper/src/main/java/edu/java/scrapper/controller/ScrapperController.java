package edu.java.scrapper.controller;

import edu.java.dto.ApiErrorResponse;
import edu.java.dto.LinkRequest;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.StateResponse;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@SuppressWarnings({"MultipleStringLiterals", "MagicNumber"})
@RestController
public class ScrapperController {

    @Autowired
    private LinkService linkService;
    @Autowired
    private ChatService chatService;

    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Чат зарегистрирован"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409",
                     description = "Чат уже существует",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(path = "/tg-chat/{id}")
    public ResponseEntity<Void> addTgChat(@Positive @PathVariable long id,
                                          @NotEmpty @RequestHeader("Name") String name) {
        boolean isSuccess = chatService.add(id, name);
        if (!isSuccess) {
            throw HttpClientErrorException.Conflict.create(HttpStatusCode.valueOf(409),
                "Чат уже существует", HttpHeaders.EMPTY, null, null);
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Чат успешно удалён"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404",
                     description = "Чат не существует",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping(path = "/tg-chat/{id}")
    public ResponseEntity<Void> deleteTgChat(@Positive @PathVariable long id) {
        boolean isSuccess = chatService.remove(id);
        if (!isSuccess) {
            throw HttpClientErrorException.NotFound.create(HttpStatusCode.valueOf(404),
                "Чат не существует", HttpHeaders.EMPTY, null, null);
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить состояние диалога")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Состояние успешно получено"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping(path = "/state/{id}")
    public ResponseEntity<StateResponse> getState(@Positive @PathVariable long id) {
        return ResponseEntity.ok(chatService.getState(id));
    }

    @Operation(summary = "Изменить состояние диалога")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Состояние успешно изменено"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(path = "/state/{id}")
    public ResponseEntity<Void> updateState(@Positive @PathVariable long id,
                                            @NotEmpty @RequestHeader("State") String state) {
        chatService.setState(id, state);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Ссылки успешно получены",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ListLinksResponse.class))),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping(path = "/links")
    public ResponseEntity<ListLinksResponse> getLinks(@Positive @RequestHeader("Tg-Chat-Id") long id) {
        return ResponseEntity.ok(linkService.listAll(id));
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Ссылка успешно добавлена"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409",
                     description = "Ссылка уже существует",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(path = "/links/add")
    public ResponseEntity<Void> addLink(@Positive @RequestHeader("Tg-Chat-Id") long id,
                                        @Valid @RequestBody LinkRequest linkRequest) {
        boolean isSuccess = linkService.add(id, linkRequest.link());
        if (!isSuccess) {
            throw HttpClientErrorException.Conflict.create(HttpStatusCode.valueOf(409),
                "Ссылка уже существует", HttpHeaders.EMPTY, null, null);
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Ссылка успешно убрана"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404",
                     description = "Ссылка не найдена",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(path = "/links/delete")
    public ResponseEntity<Void> deleteLink(@Positive @RequestHeader("Tg-Chat-Id") long id,
                                           @Valid @RequestBody LinkRequest linkRequest) {
        boolean isSuccess = linkService.remove(id, linkRequest.link());
        if (!isSuccess) {
            throw HttpClientErrorException.NotFound.create(HttpStatusCode.valueOf(404),
                "Ссылка не найдена", HttpHeaders.EMPTY, null, null);
        }
        return ResponseEntity.ok().build();
    }
}
