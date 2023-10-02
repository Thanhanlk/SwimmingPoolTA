package com.swimmingpool.order;

import com.swimmingpool.common.controller.BaseController;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.order.request.OrderSearchRequest;
import com.swimmingpool.order.response.OrderSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class AdminOrderController extends BaseController {

    private final IOrderService orderService;

    @GetMapping
    public String index(OrderSearchRequest orderSearchRequest, Model model) {
        PageResponse<OrderSearchResponse> pageResponse = this.orderService.searchOrder(orderSearchRequest);
        model.addAttribute("searchRequest", orderSearchRequest);
        this.addPagingResult(model, pageResponse);
        this.addJavascript(model, "/admin/javascript/column-controller");
        this.addCss(model, "/admin/css/column-controller", "/admin/css/user");
        return "admin/pages/order/index";
    }
}
