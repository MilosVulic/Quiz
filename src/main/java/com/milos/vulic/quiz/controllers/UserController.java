package com.milos.vulic.quiz.controllers;

import com.milos.vulic.quiz.models.OfferedAnswer;
import com.milos.vulic.quiz.models.Role;
import com.milos.vulic.quiz.models.User;
import com.milos.vulic.quiz.models.UserQuestionAnswer;
import com.milos.vulic.quiz.services.OfferedAnswerService;
import com.milos.vulic.quiz.services.RankListService;
import com.milos.vulic.quiz.services.UserQuestionAnswerService;
import com.milos.vulic.quiz.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;
    private final OfferedAnswerService offeredAnswerService;
    private final UserQuestionAnswerService userQuestionAnswerService;
    private final RankListService rankListService;

    public UserController(UserService userService, UserQuestionAnswerService userQuestionAnswerService, OfferedAnswerService offeredAnswerService, RankListService rankListService) {
        this.userService = userService;
        this.userQuestionAnswerService = userQuestionAnswerService;
        this.offeredAnswerService = offeredAnswerService;
        this.rankListService = rankListService;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        Set<User> listaUsera = new HashSet<>();
        for (User u : userService.getAllUsers()) {
            for (UserQuestionAnswer userQuestionAnswer : userQuestionAnswerService.findAll()) {
                if (userQuestionAnswer.getUser().getUserId().equals(u.getUserId())) {
                    listaUsera.add(u);
                }
            }
        }
        model.addAttribute("users", listaUsera);
        return "list-users";
    }

    @GetMapping("/users/allans")
    public String getUsersAllAnswers(Model model) {
        Set<User> listaUsera = new HashSet<>();
        Set<User> listaUseraAllAns = new HashSet<>();

        for (User u : userService.getAllUsers()) {
            for (UserQuestionAnswer userQuestionAnswer : userQuestionAnswerService.findAll()) {
                if (userQuestionAnswer.getUser().getUserId().equals(u.getUserId())) {
                    listaUsera.add(u);
                }
            }
        }

        for (User u : listaUsera) {
            for (UserQuestionAnswer userQuestionAnswer : userQuestionAnswerService.findAll()) {
                if (userQuestionAnswer.getOfferedAnswer() == null && userQuestionAnswer.getUser().getUserId().equals(u.getUserId())) {
                    listaUseraAllAns.add(u);
                }
            }
        }

        listaUsera.removeAll(listaUseraAllAns);

        model.addAttribute("users", listaUsera);
        return "list-users";
    }

    @GetMapping("/users/alltrue")
    public String getUsersAllTrue(Model model) {
        Set<User> listAllUsers = new HashSet<>(userService.getAllUsers());
        Set<User> listAllParticipatedUsers = new HashSet<>();
        Set<User> listaUsersWithNotCorrectAns = new HashSet<>();
        Set<User> listAllUsersWithCorrectAns = new HashSet<>();
        List<OfferedAnswer> listOfTrueAns = offeredAnswerService.getAllTrueOffers();
        for (OfferedAnswer o : listOfTrueAns) {
            for (UserQuestionAnswer userQuestionAnswer : userQuestionAnswerService.findAll()) {
                if (userQuestionAnswer.getQuestion().getQuestionId().equals(o.getQuestion().getQuestionId()) && (userQuestionAnswer.getOfferedAnswer() == null || !userQuestionAnswer.getOfferedAnswer().getOfferedAnswerId().equals(o.getOfferedAnswerId()))) {
                    listaUsersWithNotCorrectAns.add(userQuestionAnswer.getUser());
                }
            }
        }

        for (User u : userService.getAllUsers()) {
            for (UserQuestionAnswer userQuestionAnswer : userQuestionAnswerService.findAll()) {
                if (userQuestionAnswer.getUser().getUserId().equals(u.getUserId())) {
                    listAllParticipatedUsers.add(u);
                }
            }
        }
        listAllUsers.removeAll(listaUsersWithNotCorrectAns);

        for (User u : listAllUsers) {
            for (User u1 : listAllParticipatedUsers) {
                if (u.equals(u1)) {
                    listAllUsersWithCorrectAns.add(u);
                }
            }
        }
        model.addAttribute("users", listAllUsersWithCorrectAns);
        return "list-users";
    }


    @GetMapping("/users/success")
    public String getUsersSuccessList(Model model) {
        model.addAttribute("users", rankListService.getListOfUsers());
        return "list-users";
    }

    @PostMapping("/")
    public String login(@ModelAttribute("user") @Valid User user) {
        User u = userService.findUserByCredentials(user.getUsername(), user.getPassword());
        if (u != null) {
            if (u.getRole().name().equals("ADMIN")) {
                return "redirect:admin";
            } else {
                return "redirect:/participant/" + u.getUserId() + "/1";
            }
        }
        return "home"; //view
    }

    @RequestMapping("/admin")
    public String redirectToAdminPage() {
        return "admin";
    }

    @RequestMapping("/")
    public String login() {
        return "home";
    }

    @RequestMapping("/register")
    public String redirectToRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {

        User existing = userService.findByUsername(user.getUsername());
        if (existing != null) {
            result.rejectValue("username", null, "There is already an account registered with that username");
        } else if (user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getEmail().isEmpty() || user.getLastName().isEmpty() || user.getName().isEmpty()) {
            model.addAttribute("message", "message");
            return "register";
        }

        if (result.hasErrors()) {
            return "register";
        }
        user.setRole(Role.PARTICIPANT);
        user.setLastAccess(new Date());
        userService.registerUser(user);
        return "redirect:";
    }
}
