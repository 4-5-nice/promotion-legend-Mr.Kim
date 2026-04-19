package com.wanted.legendkim.domain.users.user.controller;

import com.wanted.legendkim.domain.users.user.model.dto.SignupDTO;
import com.wanted.legendkim.domain.users.user.model.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {

    private final MemberService memberService;

    public UserController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupDTO", new SignupDTO());
        return "user/signup";
    }

    @PostMapping("/signup")
    public ModelAndView signup(@ModelAttribute SignupDTO signupDTO, ModelAndView mv){

        // 반환 타입을 Long으로 수정
        Long result = memberService.regist(signupDTO);
        String message = null;

        if (signupDTO.getBirthDate() != null && signupDTO.getBirthDate().isAfter(java.time.LocalDate.now())) {
            mv.addObject("message", "생년월일은 미래 날짜를 선택할 수 없습니다.");
            mv.setViewName("user/signup"); // 다시 회원가입 창으로!
            return mv;
        }

        if(result == null ){
            message = "이미 가입된 이메일입니다.";
            mv.setViewName("user/signup");
        }else if(result == 0L) { // 0L로 수정
            message = "서버에서 오류가 발생하였습니다.";
            mv.setViewName("user/signup");
        }else if(result >= 1L){
            message = "회원가입이 완료되었습니다.";
            mv.setViewName("redirect:/auth/login");
        }

        mv.addObject("message", message);
        return mv;
    }
}