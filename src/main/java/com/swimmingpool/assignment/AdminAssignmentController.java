package com.swimmingpool.assignment;

import com.swimmingpool.assignment.request.AssignmentCreationRequest;
import com.swimmingpool.assignment.request.AssignmentSearchRequest;
import com.swimmingpool.assignment.response.AssignmentCreationResponse;
import com.swimmingpool.assignment.response.AssignmentSearchResponse;
import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.controller.BaseController;
import com.swimmingpool.common.dto.PageResponse;
import com.swimmingpool.common.dto.Result;
import com.swimmingpool.common.exception.BusinessException;
import com.swimmingpool.common.util.I18nUtil;
import com.swimmingpool.course.Course;
import com.swimmingpool.course.ICourseService;
import com.swimmingpool.pool.IPoolService;
import com.swimmingpool.pool.response.PoolResponse;
import com.swimmingpool.user.IUserService;
import com.swimmingpool.user.request.UserSearchRequest;
import com.swimmingpool.user.response.UserSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/assignment")
@RequiredArgsConstructor
public class AdminAssignmentController extends BaseController {

    private final IAssignmentService assignmentService;
    private final IUserService userService;
    private final ICourseService courseService;
    private final IPoolService poolService;

    @GetMapping
    public String getAssignment(AssignmentSearchRequest request, Model model) {
        PageResponse<AssignmentSearchResponse> pageResponse = this.assignmentService.searchAssignment(request);
        this.addPagingResult(model, pageResponse);
        this.addJavascript(model, "/admin/javascript/column-controller");
        this.addCss(model, "/admin/css/column-controller", "/admin/css/user");
        return "admin/pages/assignment/index";
    }

    @GetMapping("/create")
    public String getCreateAssignment(Model model) {
        List<PoolResponse> pools = this.poolService.findAllActive();
        List<UserSearchResponse> staffUser = this.userService.findStaffUser(new UserSearchRequest());
        List<Course> courses = this.courseService.findActiveCourse();

        if (!model.containsAttribute("assignmentCreation")) {
            model.addAttribute("assignmentCreation", new AssignmentCreationRequest());
        }

        model.addAttribute("teachers", staffUser);
        model.addAttribute("pools", pools);
        model.addAttribute("courses", courses);
        return "admin/pages/assignment/create";
    }

    @PostMapping
    public String assign(
            @ModelAttribute("assignmentCreation") AssignmentCreationRequest creationRequest,
            RedirectAttributes redirectAttributes
    ) {
        try {
            this.assignmentService.saveAssignment(creationRequest);
            Result<List<AssignmentCreationResponse>> result = Result.success(null, I18nUtil.getMessage("assignment.create.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/admin/assignment";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/admin/assignment/create");
            ex.setData(Map.of("assignmentCreation", creationRequest));
            throw ex;
        }

    }
}
