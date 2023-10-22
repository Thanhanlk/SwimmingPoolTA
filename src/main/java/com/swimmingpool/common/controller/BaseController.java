package com.swimmingpool.common.controller;

import com.swimmingpool.common.constant.AppConstant;
import com.swimmingpool.common.dto.PageResponse;
import org.springframework.ui.Model;

public class BaseController {

    public void addPagingResult(Model model, PageResponse<?> pageResponse) {
        model.addAttribute(AppConstant.ResponseKey.PAGING, pageResponse);
    }

    public void addJavascript(Model model, String...js) {
        model.addAttribute(AppConstant.ResponseKey.JS, js);
    }

    public void addCss(Model model, String...css) {
        model.addAttribute(AppConstant.ResponseKey.CSS, css);
    }
}
