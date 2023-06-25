package dev.be.boardadminproject.controller;

import dev.be.boardadminproject.dto.AdminAccountDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/members")
@Controller
public class AdminAccountController {

    @GetMapping
    public String members(
            Model model,
            HttpServletRequest request
    ) {
        model.addAttribute("request", request.getRequestURI());
        return "admin/members";
    }

    @ResponseBody
    @GetMapping("/api/admin/members")
    public List<AdminAccountDto.Response> getMembers() {
        return List.of();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @GetMapping("/api/admin/members/{memberId}")
    public void delete(@PathVariable Long memberId) {

    }
}
