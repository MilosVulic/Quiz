package com.milos.vulic.quiz.services;

import com.milos.vulic.quiz.models.OfferedAnswer;
import com.milos.vulic.quiz.models.User;
import com.milos.vulic.quiz.models.UserQuestionAnswer;
import com.milos.vulic.quiz.utils.MapUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RankListServiceImp implements RankListService {

    private final UserService userService;
    private final OfferedAnswerService offeredAnswerService;
    private final UserQuestionAnswerService userQuestionAnswerService;

    public RankListServiceImp(UserService userService, UserQuestionAnswerService userQuestionAnswerService, OfferedAnswerService offeredAnswerService) {
        this.userService = userService;
        this.userQuestionAnswerService = userQuestionAnswerService;
        this.offeredAnswerService = offeredAnswerService;
    }

    @Override
    public List<User> getListOfUsers() {
        Set<User> listaUsera = new HashSet<>();
        List<User> finalListUser = new ArrayList<>();
        Set<UserQuestionAnswer> listOfUsersAns = new HashSet<>();
        Map<User, Integer> mapOfUsersWithCorrectAns = new HashMap<>();
        List<OfferedAnswer> listOfTrueAns = offeredAnswerService.getAllTrueOffers();

        for (User u : userService.getAllUsers()) {
            for (UserQuestionAnswer userQuestionAnswer : userQuestionAnswerService.findAll()) {
                if (userQuestionAnswer.getUser().getUserId().equals(u.getUserId())) {
                    listaUsera.add(u);
                    listOfUsersAns.add(userQuestionAnswer);
                }
            }
        }

        for (User u : listaUsera) {
            int countCorrect = 0;
            for (OfferedAnswer o : listOfTrueAns) {
                for (UserQuestionAnswer userQuestionAnswer : listOfUsersAns) {
                    if (userQuestionAnswer.getOfferedAnswer() != null) {
                        if (userQuestionAnswer.getQuestion().getQuestionId().equals(o.getQuestion().getQuestionId()) &&
                                userQuestionAnswer.getOfferedAnswer().getOfferedAnswerId().equals(o.getOfferedAnswerId())
                                && u.getUserId().equals(userQuestionAnswer.getUser().getUserId())) {
                            countCorrect++;
                        }
                    }
                }
            }
            mapOfUsersWithCorrectAns.put(u, countCorrect);
        }

        Map<User, Integer> sortedMap = MapUtils.sortByValue(mapOfUsersWithCorrectAns);


        for (Map.Entry<User, Integer> entry : sortedMap.entrySet()) {
            finalListUser.add(entry.getKey());
        }
        Collections.reverse(finalListUser);
        return finalListUser;
    }
}
