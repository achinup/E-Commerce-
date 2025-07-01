package e_.demo.controller;

import e_.demo.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/query")
public class ChatbotController {

    @Autowired
    private ChatbotService queryInterpreterService;

    @PostMapping
    public String handlePlainTextQuery(@RequestBody String plainText) {
        return queryInterpreterService.interpretAndFetch(plainText);
    }
}
