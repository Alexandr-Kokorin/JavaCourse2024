package edu.java.bot.api;

import edu.java.bot.GodOfLinks;
import edu.java.bot.api.dto.ApiErrorResponse;
import edu.java.bot.api.dto.LinkUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {

    @Autowired
    private GodOfLinks bot;

    @Operation(summary = "Отправить обновление")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Обновление обработано"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(path = "/updates")
    public ResponseEntity<Void> sendUpdates(@Valid @RequestBody LinkUpdate linkUpdate) {
        for (long chatId : linkUpdate.tgChatIds()) {
            bot.sendMessage(
                chatId,
                List.of("По ссылке " + linkUpdate.url() + " произошло обновление." + "\n\n"
                        + "Описание:" + "\n" + linkUpdate.description())
            );
        }
        return ResponseEntity.ok().build();
    }
}
