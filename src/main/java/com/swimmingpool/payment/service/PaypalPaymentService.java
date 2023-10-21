package com.swimmingpool.payment.service;

import com.paypal.api.payments.*;
import com.swimmingpool.cart.response.CartResponse;
import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.util.CurrencyUtil;
import com.swimmingpool.course.Course;
import com.swimmingpool.order.request.CreateOrderRequest;
import com.swimmingpool.paypal.PaypalProperties;
import com.swimmingpool.paypal.PaypalService;
import com.swimmingpool.rate.IRateService;
import com.swimmingpool.rate.response.RateResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("paypalPaymentService")
@RequiredArgsConstructor
@Slf4j
public class PaypalPaymentService extends AbstractPaymentService {

    private final PaypalService paypalService;
    private final PaypalProperties paypalProperties;
    private final IRateService rateService;

    @Override
    public String payment(CreateOrderRequest createOrderRequest) {
        List<CartResponse> carts = this.getCartByIds(createOrderRequest.getCartId());
        carts
            .stream()
            .map(CartResponse::getAssignmentId)
            .map(this.assignmentService::findByIdThrowIfNotPresent)
            .forEach(this::validateAssignment);
        Payment payment = this.authorizePayment(createOrderRequest, carts);
        String approvalLink = this.paypalService.getApprovalLink(payment);
        return "redirect:" + approvalLink;
    }

    @SneakyThrows
    public Payment authorizePayment(CreateOrderRequest createOrderRequest, List<CartResponse> carts) {
        Payer payer = this.paypalService.getPayerInformation(createOrderRequest.getFirstName(), createOrderRequest.getLastName());
        String cartIds = createOrderRequest.getCartId()
                .stream()
                .map(x -> "cartId=" + x)
                .collect(Collectors.joining("&"));
        RedirectUrls redirectUrls = this.paypalService.getRedirectURLs("&_uuid=" + createOrderRequest.getUuid() +"&" + cartIds);
        List<Transaction> listTransaction = getTransactionInformation(createOrderRequest, carts);
        return this.paypalService.payment(payer, redirectUrls, listTransaction);
    }

    private List<Transaction> getTransactionInformation(CreateOrderRequest createOrderRequest, List<CartResponse> carts) {
        RateResponse rate = this.rateService.getRate();
        Map<String, BigDecimal> rates = rate.getRates();
        BigDecimal vndRate = rates.get("VND");
        vndRate.setScale(2, RoundingMode.HALF_EVEN);
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        BigDecimal totalTax = AppConstant.ZERO;
        BigDecimal subTotalPrice = AppConstant.ZERO;

        for (CartResponse cart : carts) {
            Course course = this.courseService.findByIdThrowIfNotPresent(cart.getCourseId());
            BigDecimal price = CurrencyUtil.calculateDiscountPrice(course.getPrice(), course.getDiscount());

            price = price.divide(vndRate, 2, RoundingMode.HALF_EVEN);
            Item item = this.paypalService.createItem(course.getCode() + "-" + course.getName(), price);
            subTotalPrice = subTotalPrice.add(price);
            items.add(item);
        }
        totalTax = totalTax.divide(vndRate, 2, RoundingMode.HALF_EVEN);
        itemList.setItems(items);

        BigDecimal feeShipping = AppConstant.ZERO;
        Details details = new Details();
        details.setShipping(feeShipping.toString());
        details.setSubtotal(subTotalPrice.toString());
        details.setTax(totalTax.toString());

        Amount amount = new Amount();
        amount.setCurrency(this.paypalProperties.getCurrency());
        amount.setTotal(subTotalPrice.add(feeShipping).add(totalTax).toString());
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setItemList(itemList);

        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);
        log.info("paypal transaction: {}", listTransaction);
        return listTransaction;
    }
}
