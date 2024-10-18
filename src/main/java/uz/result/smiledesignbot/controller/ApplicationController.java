package uz.result.smiledesignbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.result.smiledesignbot.dto.ApiResponse;
import uz.result.smiledesignbot.entity.Application;
import uz.result.smiledesignbot.service.ApplicationService;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Application>> created(
            @RequestBody Application application
    ) {
        return applicationService.created(application);
    }

}
