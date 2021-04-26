package com.inovus.mimimimetr.repo;

import com.inovus.mimimimetr.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CatRepo extends JpaRepository<Cat, Long> {
    List<Cat> findTop10OrByOrderByVoteScoreDesc();
    @Modifying
    @Query(nativeQuery = true,
            value = "update cat " +
                    "set vote_score = vote_score + 1 " +
                    "where id = :catId")
    void incrementScore(Long catId);
}
