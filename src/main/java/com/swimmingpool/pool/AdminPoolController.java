package com.swimmingpool.pool;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.controller.BaseController;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.pool.request.PoolCreationRequest;
import com.swimmingpool.pool.request.PoolSearchRequest;
import com.swimmingpool.pool.response.PoolResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pool")
@RequiredArgsConstructor
public class AdminPoolController extends BaseController {

    private final IPoolService poolService;

    @GetMapping
    private String index(PoolSearchRequest searchRequest, Model model) {
        PageResponse<PoolResponse> pageResponse = this.poolService.searchPool(searchRequest);
        model.addAttribute("searchRequest", searchRequest);
        this.addPagingResult(model, pageResponse);
        this.addCss(model, "/admin/css/user");
        return "admin/pages/pool/index";
    }

    @GetMapping("/change-status")
    public String changeStatus(@RequestParam String id, RedirectAttributes redirectAttributes) {
        try {
            this.poolService.changeStatus(id);
            Result<Object> result = Result.success(null, I18nUtil.getMessage("pool.status.update.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/admin/pool";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/admin/pool");
            throw ex;
        }
    }

    @PostMapping
    public String savePool(@Valid PoolCreationRequest request, RedirectAttributes redirectAttributes) {
        try {
            this.poolService.savePool(request);
            Result<Object> result = Result.success(null, I18nUtil.getMessage("pool.save.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/admin/pool";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/admin/pool");
            throw ex;
        }
    }

    @GetMapping("/delete")
    public String deletePool(@RequestParam String id, RedirectAttributes redirectAttributes) {
        try {
            this.poolService.deletePool(id);
            Result<Object> result = Result.success(null, I18nUtil.getMessage("pool.delete.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/admin/pool";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/admin/pool");
            throw ex;
        }
    }
}
