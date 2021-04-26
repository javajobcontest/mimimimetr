package com.inovus.mimimimetr.service;

import com.inovus.mimimimetr.exception.CustomException;
import com.inovus.mimimimetr.model.Cat;
import com.inovus.mimimimetr.model.User;
import com.inovus.mimimimetr.repo.CatRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CatService {
    private static Random random = new Random(0);

    private final CatRepo catRepo;

    public long catsSize() {
        return catRepo.count();
    }

    public void fillNewToVote(User user) {
        List<Cat> cats = catRepo.findAll();

        cats.removeIf(cat -> user.seenCatsId.contains(cat.getId()));

        int index = random.nextInt(cats.size());
        Cat first = cats.get(index);
        cats.remove(index);

        index = random.nextInt(cats.size());
        Cat second = cats.get(index);

        user.toVote = new User.ToVote(first.getId(), second.getId());
    }

    public List<Cat> getTop() {
        return catRepo.findTop10OrByOrderByVoteScoreDesc();
    }

    public Cat get(Long catId) throws CustomException {
        Optional<Cat> result = catRepo.findById(catId);
        if (!result.isPresent())
            throw new CustomException();
        return result.get();
    }

    public Cat add(Cat cat) {
        return catRepo.save(cat);
    }

    @Transactional
    public void addScore(Cat cat) {
        catRepo.incrementScore(cat.getId());
    }
}
