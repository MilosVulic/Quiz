package com.milos.vulic.quiz.controllers;

import com.milos.vulic.quiz.models.OfferedAnswer;
import com.milos.vulic.quiz.services.OfferedAnswerService;
import com.milos.vulic.quiz.services.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class OfferedAnswerController {

    private final OfferedAnswerService offeredAnswerService;
    private final QuestionService questionService;

    public OfferedAnswerController(OfferedAnswerService offeredAnswerService, QuestionService questionService) {
        this.offeredAnswerService = offeredAnswerService;
        this.questionService = questionService;
    }

    @RequestMapping("/offeredAnswers/{questionId}")
    public String redirectToOfferedAnswersPage(@PathVariable Long questionId, Model model) {
        model.addAttribute("offeredAnswers", offeredAnswerService.getListOfOfferedAnswersByQuestionId(questionId));
        model.addAttribute("questionId", questionId);
        return "list-offered-answers";
    }

    @RequestMapping("/offeredAnswers/new/{questionId}")
    public String redirectToCreatingOfferedAnswerPage(@PathVariable Long questionId, Model model) {
        model.addAttribute("questionId", questionId);
        return "offered-answer-new";
    }

    @PostMapping("/offeredAnswers/new/{questionId}")
    public String insertOfferedAnswer(@PathVariable Long questionId, @ModelAttribute("ans") @Valid OfferedAnswer offeredAnswer, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "offered-answer-new";
        } else if (offeredAnswerService.findCorrectAnswerByQuestionId(questionId) != null && offeredAnswer.isCorrectness()) {
            redirectAttributes.addFlashAttribute("message", "Za ovo pitanje vec postoji tacan odgovor (good try tho :D )");
            return "redirect:/offeredAnswers/new/" + questionId.toString();
        }
        offeredAnswer.setQuestion(questionService.getQuestionById(questionId));
        offeredAnswerService.addOfferedAnswer(offeredAnswer);
        return "redirect:/offeredAnswers/" + questionId.toString();
    }

    @RequestMapping(value = "/offeredAnswers/{questionId}/{offeredAnswerId}", method = RequestMethod.POST)
    public String deleteQuestion(@PathVariable Long questionId, @PathVariable Long offeredAnswerId) {
        offeredAnswerService.deleteOfferedAnswerById(offeredAnswerId);
        return "redirect:/offeredAnswers/" + questionId.toString();
    }
}