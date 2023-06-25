package dev.be.boardadminproject.controller;

import dev.be.boardadminproject.dto.MemberDto;
import dev.be.boardadminproject.service.MemberManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/management/members")
@Controller
public class MemberManagementController {

    private final MemberManagementService memberManagementService;


    @GetMapping
    public String members(Model model, HttpServletRequest request) {
        model.addAttribute("request", request.getRequestURI());
        model.addAttribute("members", memberManagementService.getMembers().stream().map(MemberDto.Response::from).toList());

        return "management/members";
    }

    @ResponseBody
    @GetMapping("/{memberId}")
    public MemberDto.Response member(@PathVariable String memberId) {
        return MemberDto.Response.from(memberManagementService.getMember(memberId));
    }

    @PostMapping("/{memberId}")
    public String deleteMember(@PathVariable String memberId) {
        memberManagementService.deleteMember(memberId);

        return "redirect:/management/members";
    }
}
