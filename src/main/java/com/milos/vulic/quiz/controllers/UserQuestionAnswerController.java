package com.milos.vulic.quiz.controllers;

import com.milos.vulic.quiz.models.*;
import com.milos.vulic.quiz.services.OfferedAnswerService;
import com.milos.vulic.quiz.services.QuestionService;
import com.milos.vulic.quiz.services.UserQuestionAnswerService;
import com.milos.vulic.quiz.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserQuestionAnswerController {

    private final UserQuestionAnswerService userQuestionAnswerService;
    private final QuestionService questionService;
    private final OfferedAnswerService offeredAnswerService;
    private final UserService userService;

    public UserQuestionAnswerController(UserQuestionAnswerService userQuestionAnswerService, QuestionService questionService, OfferedAnswerService offeredAnswerService, UserService userService) {
        this.userQuestionAnswerService = userQuestionAnswerService;
        this.questionService = questionService;
        this.offeredAnswerService = offeredAnswerService;
        this.userService = userService;
    }

    @PostMapping("/participant/{userId}/{questionNumber}/started")
    public String insertingAns(@ModelAttribute("userAnsDTO") @Valid UserAnsDTO userAnsDTO, @PathVariable Long userId, @PathVariable Long questionNumber, Model model) {

        model.addAttribute("questions", questionService.getListOfQuestions());
        model.addAttribute("userId", userId);
        model.addAttribute("questionNumber", questionNumber);

        int count = questionService.getListOfQuestions().size();
        List<Question> listica = questionService.getListOfQuestions();
        if (questionNumber == 0) {
            return "redirect:/participant/" + userId.toString() + "/" + 1;
        } else if ((questionNumber + 1) <= count) {
            insertic(userService.findByUserId(userId), listica.get(Integer.valueOf(String.valueOf(questionNumber)) - 1),
                    offeredAnswerService.findOfferedAnswerById(userAnsDTO.getOfferedAnswerId()));
            return "redirect:/participant/" + userId.toString() + "/" + (questionNumber + 1);
        } else if (((questionNumber + 1) - count) == 1) {
            insertic(userService.findByUserId(userId), listica.get(Integer.valueOf(String.valueOf(questionNumber)) - 1),
                    offeredAnswerService.findOfferedAnswerById(userAnsDTO.getOfferedAnswerId()));
            return "redirect:/participant/" + userId.toString() + "/" + (questionNumber + 1);
        } else if ((questionNumber + 1) - count == 2) {
            insertNullValues(userService.findByUserId(userId));
            return "redirect:/participant/" + userId.toString() + "/" + questionNumber;
        }
        return "redirect:/participant/" + userId.toString() + "/" + 1;
    }

    private void insertic(User user, Question question, OfferedAnswer offeredAnswer) {
        UserQuestionAnswer ua = userQuestionAnswerService.findByQuestionIdAndUserId(question.getQuestionId(), user.getUserId());
        if (ua == null) {
            UserQuestionAnswer userQuestionAnswer = new UserQuestionAnswer();
            userQuestionAnswer.setUser(user);
            userQuestionAnswer.setQuestion(question);
            userQuestionAnswer.setOfferedAnswer(offeredAnswer);
            userQuestionAnswerService.insertUserQuestionAnswer(userQuestionAnswer);
        } else {
            ua.setOfferedAnswer(offeredAnswer);
            userQuestionAnswerService.updateUserQuestionAnswer(ua);
        }
    }

    // user gave up, inserting null values to all questions that user didnt provide answer yet
    private void insertNullValues(User user) {
        List<UserQuestionAnswer> listOfUserQuestionAnswers = userQuestionAnswerService.findallByUserId(user.getUserId());
        List<Question> listOfQuestions = questionService.getListOfQuestions();
        Set<Long> setUaq = new HashSet<>();
        Set<Long> setQuestions = new HashSet<>();

        for (UserQuestionAnswer userQuestionAnswer : listOfUserQuestionAnswers) {
            setUaq.add(userQuestionAnswer.getQuestion().getQuestionId());
        }

        for (Question question : listOfQuestions) {
            setQuestions.add(question.getQuestionId());
        }

        setQuestions.removeAll(setUaq);

        for (Long q : setQuestions) {
            UserQuestionAnswer userQuestionAnswer = new UserQuestionAnswer();
            userQuestionAnswer.setUser(user);
            userQuestionAnswer.setOfferedAnswer(null);
            userQuestionAnswer.setQuestion(questionService.getQuestionById(q));
            userQuestionAnswerService.insertUserQuestionAnswer(userQuestionAnswer);
        }
    }
}



