package com.inovus.mimimimetr.controller;

import com.inovus.mimimimetr.exception.CustomException;
import com.inovus.mimimimetr.model.Cat;
import com.inovus.mimimimetr.model.CatDao;
import com.inovus.mimimimetr.model.User;
import com.inovus.mimimimetr.service.CatService;
import com.inovus.mimimimetr.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ViewsController {

    private final CatService catService;

    @GetMapping("addcat")
    public String addCat() {
        return "pages/addCat";
    }

    @PostMapping("addcat")
    public String addCat(
            @RequestParam String name,
            @RequestParam String pictureUrl,
            Model model
    ) {
        if (name == null || name.isEmpty()
                || pictureUrl == null || pictureUrl.isEmpty()
        ) {
            model.addAttribute("message", "Форма заполнена неверно...");
            return "pages/addCat";
        }

        Cat cat = new Cat();
        cat.setName(name);
        cat.setPictureUrl(pictureUrl);

        Cat result = catService.add(cat);

        if (result != null)
            model.addAttribute("message", "Котик успешно добавлен!");
        else
            model.addAttribute("message", "Не удалось добавить котика...");

        return "pages/addCat";
    }

    @GetMapping("vote")
    public String vote(
            HttpServletRequest request,
            Model model
    ) throws CustomException {
        if (catService.catsSize() < Constants.MIN_SIZE_TO_VOTE)
            throw new CustomException("Перед голосованием необходимо хотя бы " + Constants.MIN_SIZE_TO_VOTE + " котиков");

        User user = User.getFromSession(request.getSession());

        if (user == null) {
            user = new User();
        } else if (user.seenCatsId.size() == Constants.MAX_SEEN_CATS) {
            model.addAttribute("cats", catService.getTop());
            return "pages/top";
        }

        if (user.toVote == null)
            catService.fillNewToVote(user);

        Cat first = catService.get(user.toVote.getFirstCatId());
        Cat second = catService.get(user.toVote.getSecondCatId());
        user.setToSession(request.getSession());

        model.addAttribute("first", CatDao.fromCat(first));
        model.addAttribute("second", CatDao.fromCat(second));

        return "pages/vote";
    }

    @PostMapping("vote")
    public String vote(
            @RequestParam Long votedId,
            HttpServletRequest request
    ) throws CustomException {
        User user = User.getFromSession(request.getSession());

        if (user == null)
            throw new CustomException();
        if (!user.toVote.contains(votedId))
            throw new CustomException();

        Cat cat = catService.get(votedId);
        catService.addScore(cat);

        user.seenCatsId.add(user.toVote.getFirstCatId());
        user.seenCatsId.add(user.toVote.getSecondCatId());
        user.toVote = null;

        user.setToSession(request.getSession());
        return "redirect:/vote";
    }
}
