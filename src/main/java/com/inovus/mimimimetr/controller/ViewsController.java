package com.inovus.mimimimetr.controller;

import com.inovus.mimimimetr.exception.CustomException;
import com.inovus.mimimimetr.model.Cat;
import com.inovus.mimimimetr.model.CatDto;
import com.inovus.mimimimetr.model.User;
import com.inovus.mimimimetr.service.CatService;
import com.inovus.mimimimetr.util.Constants;
import com.inovus.mimimimetr.util.PagesEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ViewsController {
    private static final String ADD_CAT_ENDPOINT = "addcat";
    private static final String VOTE_ENDPOINT = "vote";

    private final CatService catService;

    @GetMapping(ADD_CAT_ENDPOINT)
    public String addCat() {
        return PagesEndpoints.ADD_CAT_PAGE;
    }

    @PostMapping(ADD_CAT_ENDPOINT)
    public String addCat(
            @RequestParam String name,
            @RequestParam String pictureUrl,
            Model model
    ) {
        if (name == null || name.isEmpty()
                || pictureUrl == null || pictureUrl.isEmpty()
        ) {
            model.addAttribute("message", "Форма заполнена неверно...");
            return PagesEndpoints.ADD_CAT_PAGE;
        }

        Cat cat = new Cat();
        cat.setName(name);
        cat.setPictureUrl(pictureUrl);

        Cat result = catService.add(cat);

        if (result != null)
            model.addAttribute("message", "Котик успешно добавлен!");
        else
            model.addAttribute("message", "Не удалось добавить котика...");

        return PagesEndpoints.ADD_CAT_PAGE;
    }

    @GetMapping(VOTE_ENDPOINT)
    public String vote(
            HttpSession session,
            Model model
    ) throws CustomException {
        if (catService.catsSize() < Constants.MIN_SIZE_TO_VOTE)
            throw new CustomException("Перед голосованием необходимо хотя бы " + Constants.MIN_SIZE_TO_VOTE + " котиков");

        User user = User.getFromSession(session);

        if (user == null) {
            user = new User();
        } else if (user.getSeenCatsId().size() == Constants.MAX_SEEN_CATS) {
            model.addAttribute("cats", catService.getTop());
            return PagesEndpoints.TOP_PAGE;
        }

        if (user.getToVote() == null)
            catService.setNewToVote(user);

        Cat first = catService.get(user.getToVote().getFirstCatId());
        Cat second = catService.get(user.getToVote().getSecondCatId());
        user.setToSession(session);

        model.addAttribute("first", CatDto.fromCat(first));
        model.addAttribute("second", CatDto.fromCat(second));

        return PagesEndpoints.VOTE_PAGE;
    }

    @PostMapping(VOTE_ENDPOINT)
    public String vote(
            @RequestParam Long votedId,
            HttpSession session
    ) throws CustomException {
        User user = User.getFromSession(session);

        if (user == null)
            throw new CustomException();

        if (!user.getToVote().contains(votedId))
            throw new CustomException();

        Cat cat = catService.get(votedId);
        catService.incrementScore(cat);

        user.getSeenCatsId().add(user.getToVote().getFirstCatId());
        user.getSeenCatsId().add(user.getToVote().getSecondCatId());
        user.setToVote(null);

        user.setToSession(session);
        return "redirect:/" + VOTE_ENDPOINT;
    }
}
