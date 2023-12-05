package com.swimmingpool.order;

import com.swimmingpool.cart.ICartService;
import com.swimmingpool.cart.response.CartResponse;
import com.swimmingpool.common.controller.BaseController;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.util.CurrencyUtil;
import com.swimmingpool.order.request.OrderSearchRequest;
import com.swimmingpool.order.response.OrderSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController extends BaseController {

    private final IOrderService orderService;
    private final ICartService cartService;

    @GetMapping
    public String index(@AuthenticationPrincipal UserDetails userDetails, OrderSearchRequest searchRequest, Model model) {
        if (!model.containsAttribute("searchRequest")) {
            model.addAttribute("searchRequest", searchRequest);
        }
        searchRequest.setCreatedBy(userDetails.getUsername());
        PageResponse<OrderSearchResponse> pageResponse = this.orderService.searchOrder(searchRequest);
        this.addPagingResult(model, pageResponse);
        this.addCss(model, "/user/css/shopping-cart", "/user/css/paging");
        this.addJavascript(model, "/admin/javascript/order");
        return "user/pages/order/index";
    }

    @GetMapping("/checkout")
    public String checkout(@RequestParam List<String> cartId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve user-specific details here, e.g., firstname, lastname, phone from userDetails
        String firstname = userDetails.getUsername(); // Replace with the actual method to get the firstname


        // Pass the user details to the Thymeleaf template
        model.addAttribute("firstname", firstname);

        List<CartResponse> checkoutProduct = this.cartService.getMyCart(cartId);
        BigDecimal newPrice = new BigDecimal("0");
        BigDecimal oldPrice = new BigDecimal("0");

        for (CartResponse cp : checkoutProduct) {
            newPrice = newPrice.add(CurrencyUtil.calculateDiscountPrice(cp.getPrice(), cp.getDiscount()));
            oldPrice = oldPrice.add(cp.getPrice());
        }
        model.addAttribute("newPrice", newPrice);
        model.addAttribute("oldPrice", oldPrice);
        model.addAttribute("productList", checkoutProduct);
        this.addCss(model, "/user/css/checkout");
        return "/user/pages/checkout/index";
    }

}
