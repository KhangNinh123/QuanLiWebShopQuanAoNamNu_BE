package iuh.fit.payment_service.Client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CartClient {
    private final WebClient webClient;

    public CartClient(WebClient.Builder webClientBuilder, @Value("${cart.service.url}") String cartServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(cartServiceUrl).build();
    }

    public Mono<List<CartItemDTO>> getCart(Long userId, String token) {
        return webClient.get()
                .uri("/api/cart")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(CartItemDTO.class)
                .collectList();
    }

    public Mono<Void> checkout(Long userId, String token) {
        return webClient.post()
                .uri("/api/cart/checkout")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Void.class);
    }
}