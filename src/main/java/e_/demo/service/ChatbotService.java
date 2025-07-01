package e_.demo.service;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    @Autowired
    private AiClient aiClient; // Spring AI client

    /**
     * Takes a plain text query, uses the LLM to map it to the correct URL,
     * then calls the URL and returns the response as a String.
     */
    public String interpretAndFetch(String plainText) {
        // 1) Send user text + prompt to LLM to get URL
        String prompt = """
            You are an API assistant for an e-commerce application.
            Given a plain English query, generate the best API URL to call.
            
            Available endpoints:
            1) GET /api/products?page=0&size=4&sort=price,asc
            2) GET /api/products/{id}
            3) GET /api/products/search?name={name}&page=0&size=10
            4) GET /api/products/price-range?minPrice={minPrice}&maxPrice={maxPrice}
            5) GET /api/products/category/{category}?page=0&size=10
            
            Examples:
            - "Give me all products sorted by price ascending" => /api/products?page=0&size=10&sort=price,asc
            - "Show me iPhone" => /api/products/search?name=iphone&page=0&size=10
            - "Products between 100 and 500" => /api/products/price-range?minPrice=100&maxPrice=500

            User query: "%s"
            Respond with ONLY the URL to call.
            """.formatted(plainText);

        AiResponse response = aiClient.generate(prompt);

        String generatedUrl = response.getGeneration().getText().trim();

        // 2) Call the generated URL using RestTemplate
        org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();

        String apiBaseUrl = "http://localhost:8080"; // or your host

        String fullUrl = apiBaseUrl + generatedUrl;

        return restTemplate.getForObject(fullUrl, String.class);
    }
}
