package uz.result.smiledesignbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.result.smiledesignbot.dto.ApiResponse;
import uz.result.smiledesignbot.entity.Button;
import uz.result.smiledesignbot.service.CounterService;

@RestController
@RequestMapping("api/counter")
@RequiredArgsConstructor
public class CounterController {

    private final CounterService counterService;

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> addCallNumber(
            @RequestParam(value = "button") Button button
    ) {
        return counterService.addCallNumber(button);
    }

}
