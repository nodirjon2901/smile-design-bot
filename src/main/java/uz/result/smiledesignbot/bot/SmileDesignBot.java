package uz.result.smiledesignbot.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.result.smiledesignbot.entity.Application;
import uz.result.smiledesignbot.entity.Button;
import uz.result.smiledesignbot.entity.Counter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SmileDesignBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    @Value("${group.chatId}")
    private String groupChatId;

    @Value("${check.person.chatId}")
    private String checkPersonChatId;


    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
    }

    public void handleSendApplication(Application application) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(groupChatId);
        sendMessage.setParseMode("Markdown");
        sendMessage.setText(
                "*Новое заявка* \uD83D\uDCCC\n\n" +
                        "\uD83D\uDC64 *ФИО*: " + application.getName() + "\n" +
                        "\uD83D\uDCDE *Номер телефона*: " + application.getPhoneNum() + "\n" +
                        "\uD83D\uDCC6 *Дата*: " + application.getCreatedDate() + "\n" +
                        "⏱ *Время*: " + application.getCreatedTime() + "\n\n"
        );
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendCounter(List<Counter> counters, Long totalApplications) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(groupChatId);
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append("<b>Еженедельный отчет \uD83D\uDCCB</b>\n \n");
        if ((counters == null || counters.isEmpty()) && totalApplications == 0) {
            textBuilder.append("<b>Поступившие заявки:</b> 0\n");
        } else {
            Map<Button, Long> buttonCountMap = new HashMap<>();
            for (Button button : Button.values()) {
                buttonCountMap.put(button, 0L);
            }
            long totalCalls = 0;
            for (Counter counter : counters) {
                Button button = counter.getSection();
                long countCall = counter.getCountCall() != null ? counter.getCountCall() : 0;
                buttonCountMap.put(button, buttonCountMap.get(button) + countCall);
                totalCalls += countCall;
            }
            for (Map.Entry<Button, Long> entry : buttonCountMap.entrySet()) {
                textBuilder.append(String.format("<b>%s: </b> %d\n", getButtonDisplayName(entry.getKey()), entry.getValue()));
            }
            textBuilder.append(String.format("\n<b>Общее количество заявок:</b> %d\n", totalApplications));
        }

        String text = textBuilder.toString();
        sendMessage.setText(text);
        sendMessage.setParseMode("HTML");

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getButtonDisplayName(Button button) {
        return switch (button) {
            case MAKE_AN_APPOINTMENT -> "Оставить заявку";
            case GET_CONSULTATION -> "Получить консультацию";
            case SEND_APPLICATION -> "Записаться";
            case CALL_YOU_TRUST_US -> "Номер телефона в форме «Нам можно доверять!»";
            case CALL_FREE_CONSULTATION -> "Номер телефона в форме «Бесплатная консультация»";
            case CALL_FOOTER -> "Номер телефона (Footer)";
            case TELEGRAM_FOOTER -> "Telegram (Footer)";
            case FACEBOOK_FOOTER -> "Facebook (Footer)";
            case YOUTUBE_FOOTER -> "Youtube (Footer)";
            case INSTAGRAM_FOOTER -> "Instagram (Footer)";
        };
    }

    @Scheduled(cron = "0 50 18 * * *",zone = "Asia/Tashkent")
    public void sendEveningMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(checkPersonChatId);
        sendMessage.setText("Bot ishlayapti - Kechki 18:50");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Tashkent")
    public void sendMorningMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(checkPersonChatId);
        sendMessage.setText("Bot ishlayapti - Ertalab 09:00");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
