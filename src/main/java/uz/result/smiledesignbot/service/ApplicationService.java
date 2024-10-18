package uz.result.smiledesignbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.result.smiledesignbot.bot.SmileDesignBot;
import uz.result.smiledesignbot.dto.ApiResponse;
import uz.result.smiledesignbot.entity.Application;
import uz.result.smiledesignbot.repository.ApplicationRepository;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final SmileDesignBot bot;

    public ResponseEntity<ApiResponse<Application>> created(Application application) {
        ApiResponse<Application> response = new ApiResponse<>();
        Application save = applicationRepository.save(application);
        bot.handleSendApplication(save);
        response.setData(save);
        response.setMessage("Successfully saved");
        return ResponseEntity.status(201).body(response);
    }

}
