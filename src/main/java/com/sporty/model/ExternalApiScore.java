package com.sporty.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExternalApiScore {
    private int home;
    private int away;

    public void addHomeScore(int score) {
        this.home += score;
    }

    public void addAwayScore(int score) {
        this.away += score;
    }

    public String toString() {
        return home + ":" + away;
    }
}
