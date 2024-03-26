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
    public String login(
        HttpSession session,
        @ModelAttribute @Valid Login login,
        BindingResult result) {

        if (login.getAge() < 10) {
            return "underage";
        }

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "redirect:/"; // return to login page
        }

        session.setAttribute("login", login);

        return "redirect:/todo"; // which is the listing page
    } 
}
