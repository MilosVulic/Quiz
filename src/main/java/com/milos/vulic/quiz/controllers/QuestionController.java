package com.milos.vulic.quiz.controllers;

import com.milos.vulic.quiz.models.*;
import com.milos.vulic.quiz.services.*;
import com.milos.vulic.quiz.utils.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final OfferedAnswerService offeredAnswerService;
    private final UserService userService;
    private final RankListService rankListService;
    private final UserQuestionAnswerService userQuestionAnswerService;

    public QuestionController(QuestionService questionService, OfferedAnswerService offeredAnswerService, UserService userService, RankListService rankListService, UserQuestionAnswerService userQuestionAnswerService) {
        this.questionService = questionService;
        this.offeredAnswerService = offeredAnswerService;
        this.userService = userService;
        this.rankListService = rankListService;
        this.userQuestionAnswerService = userQuestionAnswerService;
    }

    // admin related

    @RequestMapping("/questions")
    public String redirectToQuestionsPage(Model model) {
        model.addAttribute("questions", questionService.getListOfQuestions());
        return "list-questions";
    }

    @RequestMapping("/questions/new")
    public String redirectToQuestionsPage() {
        return "question-new";
    }

    @PostMapping("/questions/new")
    public String insertQuestion(@ModelAttribute("ques") @Valid Question question, BindingResult result) {

        if (result.hasErrors()) {
            return "questions-new";
        }
        questionService.addQuestion(question);
        return "redirect:/questions";
    }

    @RequestMapping(value = "/questions/{questionId}", method = RequestMethod.POST)
    public String deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteQuestionById(questionId);
        return "redirect:/questions";
    }

    @RequestMapping("/questions/percentage")
    public String getUsersSuccessList(Model model) {
        List<OfferedAnswer> listOfTrueOffers = offeredAnswerService.getAllTrueOffers();
        List<UserQuestionAnswer> listaOdgovora = userQuestionAnswerService.findAll();
        Map<Question, Integer> mapOfQuestionsAndCorrecness = new HashMap<>();
        List<Question> finalListUser = new ArrayList<>();
        List<Integer> correctnessList = new ArrayList<>();

        for (OfferedAnswer o : listOfTrueOffers) {
            int correct = 0;
            for (UserQuestionAnswer u : listaOdgovora) {
                if (u.getOfferedAnswer() != null) {
                    if (u.getOfferedAnswer().getOfferedAnswerId().equals(o.getOfferedAnswerId())) {
                        correct++;
                    }
                }
            }
            mapOfQuestionsAndCorrecness.put(o.getQuestion(), correct);
        }

        Map<Question, Integer> sortedMap = MapUtils.sortByValue(mapOfQuestionsAndCorrecness);

        for (Map.Entry<Question, Integer> entry : sortedMap.entrySet()) {
            finalListUser.add(entry.getKey());
            correctnessList.add(entry.getValue());
        }
        Collections.reverse(finalListUser);
        Collections.reverse(correctnessList);

        List<QuestionDTO> listOfQuestionForView = new ArrayList<>();
        for (int i = 0; i < finalListUser.size(); i++) {
            listOfQuestionForView.add(new QuestionDTO(finalListUser.get(i).getQuestionId(), finalListUser.get(i).getQuestion(), correctnessList.get(i)));
        }
        model.addAttribute("questions", listOfQuestionForView);
        return "list-questions-percentage";
    }

    // participant related

    @RequestMapping("/participant/{userId}/{questionNumber}")
    public String redirectToUserHomePage(@PathVariable Long userId, @PathVariable Long questionNumber, RedirectAttributes redirectAttributes, Model model) {
        int count = questionService.getListOfQuestions().size();

        if (count > 0 && questionNumber <= count) {
            List<Question> listQuestion = questionService.getListOfQuestions();
            Question question = listQuestion.get(Integer.valueOf(String.valueOf(questionNumber)) - 1);

            model.addAttribute("userId", userId);
            model.addAttribute("questionNumber", questionNumber);
            model.addAttribute("question", question);
            model.addAttribute("numberOfQuestions", count);
            model.addAttribute("offeredAns", offeredAnswerService.getListOfOfferedAnswersByQuestionId(question.getQuestionId()));

            User user = userService.findByUserId(userId);
            user.setLastAccess(new Date());
            userService.registerUser(user);
            return "participant";
        } else if (count == 0) {
            model.addAttribute("userId", userId);
            model.addAttribute("questionNumber", questionNumber);
            redirectAttributes.addFlashAttribute("message", "Za quiz trenutno ne postoje pitanja xd");
            return "participant";
        } else {
            List<User> list = rankListService.getListOfUsers();
            List<User> finalList = new ArrayList<>();
            int listSize = list.size();
            if (listSize >= 5) {
                for (int i = 0; i < 5; i++) {
                    finalList.add(list.get(i));
                }
                model.addAttribute("ranklist", finalList);
            } else {
                finalList.addAll(list);
                model.addAttribute("ranklist", finalList);
            }
            System.out.println("Ajde da vidim sad iz liste " +  finalList.get(0).getUserId());
            System.out.println("Ajde da vidim sad ovo je ovako id " + userId);
            if(!finalList.isEmpty() && !list.isEmpty() && finalList.get(0).getUserId().equals(userId)){
                System.out.println("Uso sam ovde ");
                model.addAttribute("cestitke", "cestitke");
            }
            model.addAttribute("questionNumberSpecial", questionNumber);
            return "participant";
        }
    }
}
