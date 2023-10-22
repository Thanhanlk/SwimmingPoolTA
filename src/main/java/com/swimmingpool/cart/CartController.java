package com.swimmingpool.cart;

import com.swimmingpool.cart.response.CartResponse;
import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.controller.BaseController;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.util.I18nUtil;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController extends BaseController {

    private final ICartService cartService;

    @GetMapping
    public String index(Model model) {
        List<CartResponse> myCart = this.cartService.getMyCart();
        model.addAttribute("myCart", myCart);
        this.addCss(model, "/user/css/shopping-cart");
        this.addJavascript(model, "/user/javascript/cart");
        return "user/pages/cart/index";
    }

    @PostMapping
    public String addToCart(@RequestParam("assignmentId") List<String> assignmentIds) {
        this.cartService.addToCart(assignmentIds);
        return "redirect:/cart";
    }

    @GetMapping("/delete")
    public String deleteCart(@RequestParam String cId, RedirectAttributes redirectAttributes) {
        try {
            this.cartService.deleteById(cId);
            Result<Object> result = Result.success(null, I18nUtil.getMessage("cart.delete.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/cart";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/cart");
            throw ex;
        }
    }

    @GetMapping("/delete-all")
    public String deleteAllCart(RedirectAttributes redirectAttributes) {
        try {
            this.cartService.deleteAllCart();
            Result<Object> result = Result.success(null, I18nUtil.getMessage("cart.delete.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/cart";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/cart");
            throw ex;
        }
    }
}
