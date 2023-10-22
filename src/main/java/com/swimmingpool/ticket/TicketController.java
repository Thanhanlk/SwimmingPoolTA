package com.swimmingpool.ticket;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.controller.BaseController;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.ticket.request.CreateTicketRequest;
import com.swimmingpool.ticket.request.SearchTicketRequest;
import com.swimmingpool.ticket.request.TicketRequest;
import com.swimmingpool.ticket.request.TicketWithReasonRequest;
import com.swimmingpool.ticket.response.TicketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController extends BaseController {
    private final ITicketService ticketService;

    @GetMapping
    public String index(SearchTicketRequest searchRequest, Model model) {
        model.addAttribute("searchRequest", searchRequest);
        PageResponse<TicketResponse> pageResponse = this.ticketService.searchTicket(searchRequest);
        this.addPagingResult(model, pageResponse);
        this.addJavascript(model, "/admin/javascript/column-controller");
        this.addCss(model, "/admin/css/column-controller", "/admin/css/user");
        return "/admin/pages/ticket/index";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public String save(@Valid CreateTicketRequest createTicketRequest, RedirectAttributes redirectAttributes) {
        try {
            this.ticketService.createTicket(createTicketRequest);
            Result<Object> result = Result.success(null, I18nUtil.getMessage("ticket.create.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/ticket";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/ticket");
            throw ex;
        }
    }

    @PostMapping("/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String reject(@Valid TicketWithReasonRequest ticket, RedirectAttributes redirectAttributes) {
        try {
            this.ticketService.rejectTicket(ticket);
            Result<Object> result = Result.success(null, I18nUtil.getMessage("ticket.reject.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/ticket";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/ticket");
            throw ex;
        }
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String approve(@Valid TicketRequest ticket, RedirectAttributes redirectAttributes) {
        try {
            this.ticketService.approveTicket(ticket);
            Result<Object> result = Result.success(null, I18nUtil.getMessage("ticket.approve.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/ticket";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/ticket");
            throw ex;
        }
    }

    @PostMapping("/cancel")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String cancel(@Valid TicketWithReasonRequest ticket, RedirectAttributes redirectAttributes) {
        try {
            this.ticketService.cancelTicket(ticket);
            Result<Object> result = Result.success(null, I18nUtil.getMessage("ticket.cancel.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/ticket";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/ticket");
            throw ex;
        }
    }
}
