package ibf2023.ssf.practice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ibf2023.ssf.practice.model.Login;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping
public class LoginController {

    // Get login page
    @GetMapping
    public ModelAndView getLoginForm() {
        Login login = new Login();
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("newLogin", login);
        return mav;
    }

    @PostMapping
    public ModelAndView login(
        HttpSession session,
        @ModelAttribute("newLogin") @Valid Login login,
        BindingResult result) {

        ModelAndView mav = new ModelAndView("login");

        if (result.hasErrors()) {
            mav.addObject("newLogin", login);
        } else {
            mav.addObject("newLogin", new Login());

            if (login.getAge() < 10) {
                mav.setViewName("underage");
            }
    
            session.setAttribute("login", login);
    
            mav.setViewName("redirect:/todo");
        }

        return mav; // which is the listing page
    } 
}
