package com.swimmingpool.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.order.OrderConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext;
    private final PaypalProperties paypalProperties;

    public Payment payment(Payer payer, RedirectUrls redirectUrls, List<Transaction> listTransaction) throws PayPalRESTException {
        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payer);
        requestPayment.setIntent("sale");
        return requestPayment.create(apiContext);
    }

    public Payer getPayerInformation(String firstName, String lastName) {
        PayerInfo payerInfo = new PayerInfo()
                .setFirstName(firstName)
                .setLastName(lastName);
        return new Payer()
                .setPaymentMethod(OrderConstant.MethodPayment.PAYPAL.name())
                .setPayerInfo(payerInfo);
    }

    public RedirectUrls getRedirectURLs(String queryString) {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(this.paypalProperties.getCancelUrl() + "?@x@=" + queryString);
        redirectUrls.setReturnUrl(this.paypalProperties.getSuccessUrl() + "?@x@=" + queryString);
        return redirectUrls;
    }

    public Item createItem(String name, BigDecimal price) {
        Item item = new Item();
        item.setCurrency(this.paypalProperties.getCurrency());
        item.setName(name);
        item.setPrice(price.toString());
        item.setTax(AppConstant.ZERO.toString());
        item.setQuantity(String.valueOf(1));
        return item;
    }

    public String getApprovalLink(Payment approvedPayment) {
        return approvedPayment.getLinks().stream()
                .filter(link -> "approval_url".equalsIgnoreCase(link.getRel()))
                .map(Links::getHref)
                .findFirst()
                .orElse(null);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }
}
