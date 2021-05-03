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
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CatService {
    private final CatRepo catRepo;
    private static final Random random = new Random(0);

    public Cat add(Cat cat) {
        return catRepo.save(cat);
    }

    public Cat get(Long catId) throws CustomException {
        Optional<Cat> result = catRepo.findById(catId);
        if (!result.isPresent())
            throw new CustomException();
        return result.get();
    }

    public List<Cat> getTop() {
        return catRepo.findTop10OrByOrderByVoteScoreDesc();
    }

    public long catsSize() {
        return catRepo.count();
    }

    @Transactional
    public void incrementScore(Cat cat) {
        catRepo.incrementScore(cat.getId());
    }

    public void setNewToVote(User user) {
        List<Cat> availableCats = catRepo.findAll();
        availableCats.removeIf(cat -> user.getSeenCatsId().contains(cat.getId()));

        Supplier<Cat> getRandomCat = () -> {
            int index = random.nextInt(availableCats.size());
            return availableCats.remove(index);
        };

        Cat first = getRandomCat.get();
        Cat second = getRandomCat.get();

        user.setToVote(new User.ToVote(first.getId(), second.getId()));
    }
}
