package dev.be.boardadminproject.controller;

import dev.be.boardadminproject.dto.AdminAccountDto;
import dev.be.boardadminproject.service.AdminAccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminAccountController {

    private final AdminAccountService adminAccountService;


    @GetMapping("/admin/members")
    public String members(Model model, HttpServletRequest request) {
        model.addAttribute("request", request.getRequestURI());
        model.addAttribute("admin_accounts", adminAccountService.members().stream().map(AdminAccountDto.Response::from).toList());
        return "admin/members";
    }

    @ResponseBody
    @GetMapping("/api/admin/members")
    public List<AdminAccountDto.Response> getMembers() {
        return adminAccountService.members().stream()
                .map(AdminAccountDto.Response::from)
                .toList();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @DeleteMapping("/api/admin/members/{memberId}")
    public void delete(@PathVariable String memberId) {
        adminAccountService.deleteMember(memberId);

    }
}
