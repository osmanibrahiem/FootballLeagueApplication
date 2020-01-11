package io.osman.football.league.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompetitionsResponse {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("competitions")
    @Expose
    private List<Competition> competitions = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Competition> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<Competition> competitions) {
        this.competitions = competitions;
    }

}