package com.swimmingpool.payment;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.exception.ValidationException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.order.OrderConstant;
import com.swimmingpool.order.request.CreateOrderRequest;
import com.swimmingpool.payment.service.IPaymentService;
import com.swimmingpool.paypal.PaypalService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentFactory paymentFactory;
    private final PaypalService paypalService;

    @PostMapping
    public String payment(@Valid CreateOrderRequest createOrderRequest, @AuthenticationPrincipal UserDetails userDetails, HttpSession httpSession) {
        try {
            createOrderRequest.setUuid(UUID.randomUUID().toString());
            createOrderRequest.setCreatedBy(userDetails.getUsername());
            IPaymentService instance = this.paymentFactory.getInstance(createOrderRequest.getMethod());
            String payment = instance.payment(createOrderRequest);
            httpSession.setAttribute(createOrderRequest.getUuid(), createOrderRequest);
            return payment;
        } catch (BusinessException ex) {
            String ids = createOrderRequest.getCartId()
                    .stream()
                    .map(id -> String.format("cartId=%s", id))
                    .collect(Collectors.joining("&"));
            ex.setRedirectUrl("redirect:/order/checkout?"+ids);
            throw ex;
        }
    }

    @GetMapping("/cancel")
    public String cancel(@RequestParam("_uuid") String uuid, HttpSession session, HttpServletRequest request) {
        session.removeAttribute(uuid);
        return "redirect:/order/checkout?" + request.getQueryString();
    }

    @GetMapping("/execute")
    public String executePayment(
        @RequestParam("_uuid") String uuid,
        @RequestParam("paymentId") String paymentId,
        @RequestParam("PayerID") String payerId,
        HttpSession httpSession
    ) {
        Object dataCreateOrder = httpSession.getAttribute(uuid);
        if (Objects.isNull(dataCreateOrder)) {
            throw new ValidationException("order.execute.not-created");
        }
        CreateOrderRequest createOrderRequest = (CreateOrderRequest) dataCreateOrder;
        try {
            Payment payment = this.paypalService.executePayment(paymentId, payerId);
            createOrderRequest.setPaymentId(payment.getId());

            IPaymentService instance = this.paymentFactory.getInstance(OrderConstant.MethodPayment.PAYPAL);
            instance.createOrder(createOrderRequest);
            httpSession.removeAttribute(uuid);
            return "redirect:/order";
        } catch (PayPalRESTException ex) {
            log.error(ex.getMessage(), ex);
            String ids = createOrderRequest.getCartId()
                    .stream()
                    .map(id -> String.format("cartId=%s", id))
                    .collect(Collectors.joining("&"));
            throw new BusinessException(I18nUtil.getMessage("payment.paypal.pay.fail"), "redirect:/order/checkout?" + ids);
        } catch (BusinessException ex) {
            String ids = createOrderRequest.getCartId()
                    .stream()
                    .map(id -> String.format("cartId=%s", id))
                    .collect(Collectors.joining("&"));
            ex.setRedirectUrl("redirect:/order/checkout?" + ids);
            ex.setData(Map.of("createOrderRequest", createOrderRequest));
            throw ex;
        }
    }
}
