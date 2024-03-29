package edu.java.scrapper.api;

import edu.java.dto.ApiErrorResponse;
import edu.java.dto.LinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.StateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("MultipleStringLiterals")
@RestController
public class ScrapperController {

    private final static Logger LOGGER = LogManager.getLogger();

    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Чат зарегистрирован"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(path = "/tg-chat/{id}")
    public ResponseEntity<Void> addTgChat(@Positive @PathVariable long id) {
        // добавить чат в БД
        LOGGER.info("Чат " + id + " успешно добавлен.");
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
        // удалить чат из БД
        LOGGER.info("Чат " + id + " успешно удален.");
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
        // Достать состояние из БД и вернуть в ответе
        return ResponseEntity.ok().build();
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
        // Обновить состояние в БД
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
        // Достать ссылки из БД и вернуть в ответе
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Ссылка успешно добавлена",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = LinkResponse.class))),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(path = "/links/add")
    public ResponseEntity<LinkResponse> addLink(@Positive @RequestHeader("Tg-Chat-Id") long id,
                                                @Valid @RequestBody LinkRequest linkRequest) {
        // Добавить ссылку в БД
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Ссылка успешно убрана",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = LinkResponse.class))),
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
    public ResponseEntity<LinkResponse> deleteLink(@Positive @RequestHeader("Tg-Chat-Id") long id,
                                                   @Valid @RequestBody LinkRequest linkRequest) {
        // Удалить ссылку из БД
        return ResponseEntity.ok().build();
    }
}
