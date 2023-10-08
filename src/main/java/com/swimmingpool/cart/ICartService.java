package com.swimmingpool.cart;

import com.swimmingpool.cart.response.CartResponse;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface ICartService {

    void addToCart(List<String> assignmentIds);

    List<CartResponse> getMyCart(List<String> carts);

    @Cacheable(
        cacheManager = "redisCacheManager",
        cacheNames = "CART",
        key="T(org.springframework.security.core.context.SecurityContextHolder).getContext()?.authentication?.name",
        unless = "#result eq null or #result.empty"
    )
    default List<CartResponse> getMyCart() {
        return this.getMyCart(null);
    }

    void deleteById(String cId);

    void deleteAllCart();
}
