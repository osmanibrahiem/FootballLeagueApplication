package io.osman.football.league.repository;

import android.content.Context;
import android.util.Log;

import io.osman.football.league.model.CompetitionsResponse;
import io.osman.football.league.model.Team;
import io.osman.football.league.model.TeamsResponse;
import io.osman.football.league.repository.api.FootballAPI;
import io.osman.football.league.repository.api.FootballClient;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FootballRepository {

    private static final String LOG_TAG = FootballRepository.class.getSimpleName();
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static FootballRepository sInstance;
    private final FootballAPI mFootballApi;

    private FootballRepository(Context context) {
        mFootballApi = FootballClient.getInstance(context).getService();
    }

    public synchronized static FootballRepository getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new FootballRepository(context);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public Observable<Response<CompetitionsResponse>> fetchCompetitionList() {
        return mFootballApi.getCompetitions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<TeamsResponse>> fetchTeamList(int competitionID) {
        return mFootballApi.getTeams(competitionID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<Team>> fetchTeamInformation(int teamID) {
        return mFootballApi.getTeamInformation(teamID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
