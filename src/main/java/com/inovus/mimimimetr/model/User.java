package com.inovus.mimimimetr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class User {

    public List<Long> seenCatsId;
    public ToVote toVote;

    public User() {
        this.seenCatsId = new ArrayList<>();
    }

    public static User getFromSession(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    public void setToSession(HttpSession session) {
        session.setAttribute("user", this);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ToVote {
        private Long firstCatId;
        private Long secondCatId;

        public boolean contains(Long catId) {
            return catId != null && (catId.equals(firstCatId) || catId.equals(secondCatId));
        }
    }

}
