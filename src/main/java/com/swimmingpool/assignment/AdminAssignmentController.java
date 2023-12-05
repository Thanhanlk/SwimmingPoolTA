package com.swimmingpool.assignment;

import com.swimmingpool.assignment.request.AssignmentCreationRequest;
import com.swimmingpool.assignment.request.AssignmentField;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        List<PoolResponse> pools = this.poolService.findAllActive();
        List<Course> courses = this.courseService.findActiveCourse();

        model.addAttribute("pools", pools);
        model.addAttribute("courses", courses);
        model.addAttribute("searchRequest", request);
        this.addPagingResult(model, pageResponse);
        this.addJavascript(model, "/admin/javascript/column-controller");
        this.addCss(model, "/admin/css/column-controller", "/admin/css/user");
        return "admin/pages/assignment/index";
    }

    @PostMapping("/add-row")
    public String addRowAssignment(
            @ModelAttribute("assignmentCreation") AssignmentCreationRequest creationRequest,
            @RequestParam(required = false, name = "userId") Optional<String> userId,
            RedirectAttributes redirectAttributes) {
        creationRequest.addAssignmentField(new AssignmentField());
        redirectAttributes.addFlashAttribute("assignmentCreation", creationRequest);
        return userId.map(i -> "redirect:/admin/assignment/update?userId=" + i)
                .orElse("redirect:/admin/assignment/create");
    }

    @PostMapping("/delete-row")
    public String deleteRowAssignment(
            @ModelAttribute("assignmentCreation") AssignmentCreationRequest creationRequest,
            @RequestParam Integer index,
            @RequestParam(required = false) Optional<String> userId,
            RedirectAttributes redirectAttributes
    ) {
        String url = userId.map(i -> "redirect:/admin/assignment/update?userId=" + i).orElse("redirect:/admin/assignment/create");
        Optional.ofNullable(creationRequest.getAssignmentField(index))
                .map(AssignmentField::getStartDate)
                .filter(x -> x.before(new Date()))
                .ifPresent(x -> {
                    throw new BusinessException(
                            "assignment.start-date.delete.past",
                            url,
                            Map.of("assignmentCreation", creationRequest)
                    );
                });
        creationRequest.deleteAssignmentField(index);
        redirectAttributes.addFlashAttribute("assignmentCreation", creationRequest);
        return url;
    }


    private String openForm(Model model) {
        List<PoolResponse> pools = this.poolService.findAllActive();
        List<UserSearchResponse> staffUser = this.userService.findStaffUser(new UserSearchRequest());
        List<Course> courses = this.courseService.findActiveCourse();
        model.addAttribute("teachers", staffUser);
        model.addAttribute("pools", pools);
        model.addAttribute("courses", courses);

        this.addJavascript(model, "/admin/javascript/assignment");
        return "admin/pages/assignment/create";
    }

    @GetMapping("/create")
    public String getCreateAssignment(Model model) {
        if (!model.containsAttribute("assignmentCreation")) {
            model.addAttribute("assignmentCreation", new AssignmentCreationRequest());
        }
        return this.openForm(model);
    }

    @GetMapping("/update")
    public String getCreateAssignment(@RequestParam String userId,  Model model) {
        try {
            if (!model.containsAttribute("assignmentCreation")) {
                AssignmentCreationRequest assignmentCreationRequest = this.assignmentService.convertToAssignmentCreationRequest(userId);
                model.addAttribute("assignmentCreation", assignmentCreationRequest);
            }
            return openForm(model);
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/admin/assignment");
            throw ex;
        }
    }

    @PostMapping("/create")
    public String createAssignment(
            @ModelAttribute("assignmentCreation") @Valid AssignmentCreationRequest creationRequest,
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

    @PostMapping("/update")
    public String updateAssignment(
            @ModelAttribute("assignmentCreation") @Valid AssignmentCreationRequest creationRequest,
            RedirectAttributes redirectAttributes
    ) {
        try {
            this.assignmentService.updateAssignment(creationRequest);
            Result<List<AssignmentCreationResponse>> result = Result.success(null, I18nUtil.getMessage("assignment.update.success"));
            redirectAttributes.addFlashAttribute(AppConstant.ResponseKey.RESULT, result);
            return "redirect:/admin/assignment";
        } catch (BusinessException ex) {
            ex.setRedirectUrl("redirect:/admin/assignment/update?userId=" + creationRequest.getUserId());
            ex.setData(Map.of("assignmentCreation", creationRequest));
            throw ex;
        }
    }
}
