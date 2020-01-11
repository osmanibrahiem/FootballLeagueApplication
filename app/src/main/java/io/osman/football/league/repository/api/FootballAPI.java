package io.osman.football.league.repository.api;

import io.osman.football.league.model.CompetitionsResponse;
import io.osman.football.league.model.Team;
import io.osman.football.league.model.TeamsResponse;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FootballAPI {

    @GET("competitions")
    Observable<Response<CompetitionsResponse>> getCompetitions();

    @GET("competitions/{id}/teams")
    Observable<Response<TeamsResponse>> getTeams(@Path("id") int id);

    @GET("teams/{id}")
    Observable<Response<Team>> getTeamInformation(@Path("id") int id);

}
