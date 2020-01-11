package io.osman.football.league.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamsResponse {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("competition")
    @Expose
    private Competition competition;
    @SerializedName("teams")
    @Expose
    private List<Team> teams = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}