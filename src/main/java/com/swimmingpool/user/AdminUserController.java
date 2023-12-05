package com.swimmingpool.user;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.controller.BaseController;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.user.request.StaffRegisterRequest;
import com.swimmingpool.user.request.UserSearchRequest;
import com.swimmingpool.user.response.UserResponse;
import com.swimmingpool.user.response.UserSearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController extends BaseController {

    private final IUserService userService;

    @GetMapping("/staff")
    public String getStaff(UserSearchRequest userSearchRequest, Model model) {
        userSearchRequest.setRole(UserConstant.Role.TEACHER);
        PageResponse<UserSearchResponse> pageResponse = this.userService.searchUser(userSearchRequest);
        model.addAttribute("title", "Quản lý giáo viên");
        model.addAttribute("subTitle", "Danh sách giáo viên");
        return this.index(model, pageResponse, userSearchRequest);
    }

    @GetMapping(value = "/staff", params = "id")
    public String getUpdateStaff(@RequestParam String id, Model model) {
        try {
            if (!model.containsAttribute("staffCreation")) {
                User user = this.userService.findByIdThrowIfNotPresent(id);
                String fullName = user.getFullName();
                int i = fullName.lastIndexOf(" ");
                String firstName = fullName.substring(i);
                String lastName = fullName.substring(0, i + 1);
                StaffRegisterRequest staffRegisterRequest = new StaffRegisterRequest();
                staffRegisterRequest.setId(user.getId());
                staffRegisterRequest.setUsername(user.getUsername());
                staffRegisterRequest.setPassword(user.getPassword());
                staffRegisterRequest.setPhone(user.getPhone());
                staffRegisterRequest.setEmail(user.getEmail());
                staffRegisterRequest.setAddress(user.getAddress());
                staffRegisterRequest.setFirstName(firstName);
                staffRegisterRequest.setLastName(lastName);
                model.addAttribute("staffCreation", staffRegisterRequest);
            }
            return "admin/pages/user/create";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/admin/user/staff");
            throw ex;
        }
    }

    @GetMapping("/staff/create")
    public String getCreateStaff(Model model) {
        if (!model.containsAttribute("staffCreation")) {
            model.addAttribute("staffCreation", new StaffRegisterRequest());
        }
        return "admin/pages/user/create";
    }

    @PostMapping("staff")
    public String postCreateStaff(@ModelAttribute("staffCreation") @Valid StaffRegisterRequest staffRegisterRequest, RedirectAttributes redirectAttributes) {
        try {
            Result<UserResponse> result = this.userService.registerUser(staffRegisterRequest);
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/admin/user/staff";
        } catch (BusinessException ex) {
            ex.setData(Map.of("staffCreation", staffRegisterRequest));
            ex.setRedirectUrl("redirect:/admin/user/staff/create");
            throw ex;
        }
    }

    @GetMapping("/customer")
    public String getCustomer(UserSearchRequest userSearchRequest, Model model) {
        userSearchRequest.setRole(UserConstant.Role.USER);
        PageResponse<UserSearchResponse> pageResponse = this.userService.searchUser(userSearchRequest);
        model.addAttribute("title", "Quản lý học viên");
        model.addAttribute("subTitle", "Danh sách học viên");
        return this.index(model, pageResponse, userSearchRequest);
    }

    @GetMapping("/change-status")
    public String changeStatus(@RequestParam String id, @RequestParam String role, RedirectAttributes redirectAttributes) {
        try {
            this.userService.changeStatus(id);
            Result<Object> result = Result.success(null, I18nUtil.getMessage("user.status.update.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            if (UserConstant.Role.USER.name().equalsIgnoreCase(role)) {
                return "redirect:/admin/user/customer";
            }
            return "redirect:/admin/user/staff";

        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/admin/user/" + role.toLowerCase());
            throw ex;
        }
    }

    private String index(Model model, PageResponse pageResponse, UserSearchRequest userSearchRequest) {
        model.addAttribute("searchRequest", userSearchRequest);
        this.addPagingResult(model, pageResponse);
        this.addJavascript(model, "/admin/javascript/column-controller");
        this.addCss(model, "/admin/css/column-controller", "/admin/css/user");
        return "admin/pages/user/index";
    }
}
