package uz.result.smiledesignbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.result.smiledesignbot.bot.SmileDesignBot;
import uz.result.smiledesignbot.dto.ApiResponse;
import uz.result.smiledesignbot.entity.Button;
import uz.result.smiledesignbot.entity.Counter;
import uz.result.smiledesignbot.repository.ApplicationRepository;
import uz.result.smiledesignbot.repository.CounterRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final CounterRepository counterRepository;

    private final ApplicationRepository applicationRepository;

    private final SmileDesignBot bot;

    public ResponseEntity<ApiResponse<?>> addCallNumber(Button button) {
        ApiResponse<?> response = new ApiResponse<>();
        Counter counter = Counter.builder()
                .section(button)
                .countCall(1L)
                .build();
        counterRepository.save(counter);
        response.setMessage("Success. Button " + button.name() + " count incremented");
        return ResponseEntity.status(201).body(response);
    }

//    @Scheduled(cron = "0 */2 * * * *")//every time
    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Tashkent")
    public void checkAndSendCounter() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);

        List<Counter> counterList = counterRepository.findByCreatedDateBetween(oneWeekAgo, now);

        Map<Button, Long> aggregatedCounters=new HashMap<>();
        for (Counter counter : counterList) {
            aggregatedCounters.put(
                    counter.getSection(),
                    aggregatedCounters.getOrDefault(counter.getSection(), 0L)+counter.getCountCall()
            );
        }

        List<Counter> savedCounters=new ArrayList<>();
        for (Map.Entry<Button, Long> entry : aggregatedCounters.entrySet()) {
            Counter aggregatedCounter = Counter.builder()
                    .section(entry.getKey())
                    .countCall(entry.getValue())
                    .build();
            savedCounters.add(aggregatedCounter);
        }

        counterRepository.deleteAll(counterList);

        Long applicationCount = applicationRepository.countInWeek(oneWeekAgo, now);
        bot.sendCounter(savedCounters, applicationCount);
    }

}
