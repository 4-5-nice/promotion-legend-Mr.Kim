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
        return "user/signup"; // void 대신 명시적으로 경로를 리턴해주는 것이 더 안전합니다.
    }

    @PostMapping("/signup")
    public ModelAndView signup(@ModelAttribute SignupDTO signupDTO, ModelAndView mv){

        // 반환 타입을 Long으로 수정
        Long result = memberService.regist(signupDTO);
        String message = null;

        if(result == null ){
            message = "이미 가입된 이메일입니다.";
            mv.setViewName("user/signup"); // 중복 시 다시 회원가입 페이지로
        }else if(result == 0L) { // 0L로 수정
            message = "서버에서 오류가 발생하였습니다.";
            mv.setViewName("user/signup");
        }else if(result >= 1L){
            message = "회원가입이 완료되었습니다.";
            // 로그인 페이지로 리다이렉트 하는 것이 좋습니다. (url 경로 주의)
            mv.setViewName("redirect:/auth/login");
        }

        mv.addObject("message", message);
        return mv;
    }
}
