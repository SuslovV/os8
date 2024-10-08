package ru.suslov.http_logging_spring_boot_starter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.suslov.http_logging_spring_boot_starter.dto.HttpRequestLogDto;
import ru.suslov.http_logging_spring_boot_starter.service.HttpRequestLogService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class HttpRequestLogController {
    private final Logger logger = LoggerFactory.getLogger(HttpRequestLogController.class.getName());
    private final HttpRequestLogService httpRequestLogService;
    private final ModelMapper modelMapper;
    private final Type listType = new TypeToken<List<HttpRequestLogDto>>() {
    }.getType();

    @Autowired
    public HttpRequestLogController(HttpRequestLogService httpRequestLogService, ModelMapper modelMapper) {
        this.httpRequestLogService = httpRequestLogService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get a list of HttpRequestLog")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = HttpRequestLogDto.class)),
                            mediaType = "application/json")
            })
    })
    @GetMapping("/httprequestlog")
    public List<HttpRequestLogDto> resources(@PageableDefault(size = 100, sort = "id") Pageable pageable, HttpServletRequest request, Map<String, String> headers) {
        return modelMapper.map(httpRequestLogService.findAll(pageable).getContent(), listType);
    }
}
