package io.osman.football.league.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.osman.football.league.helper.CompetitionUtils;
import io.osman.football.league.model.Competition;
import io.osman.football.league.model.CompetitionsResponse;
import io.osman.football.league.model.Player;
import io.osman.football.league.model.Team;
import io.osman.football.league.model.TeamsResponse;
import io.osman.football.league.repository.FootballRepository;
import io.osman.football.league.repository.api.ResponseObserver;
import io.osman.football.league.ui.base.BaseViewModel;

public class FootballViewModel extends BaseViewModel {

    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final FootballRepository mRepository;
    private MutableLiveData<List<Competition>> mCompetitionList = new MutableLiveData<>();
    private MutableLiveData<Competition> mCompetition = new MutableLiveData<>();
    private MutableLiveData<List<Team>> mTeamList = new MutableLiveData<>();
    private MutableLiveData<Team> mTeam = new MutableLiveData<>();
    private MutableLiveData<List<Player>> mPlayerList = new MutableLiveData<>();

    public FootballViewModel(@NonNull Application application) {
        super(application);
        mRepository = FootballRepository.getInstance(application);
    }

    public LiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading.setValue(isLoading);
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        errorMessage.setValue(message);
    }

    public LiveData<Competition> getCompetition() {
        return mCompetition;
    }

    public void setCompetition(Competition competition) {
        mCompetition.setValue(competition);
    }

    public LiveData<List<Competition>> getCompetitionList() {
        return mCompetitionList;
    }

    public void fetchCompetitionList() {
        setIsLoading(true);
        mRepository.fetchCompetitionList().subscribe(new ResponseObserver<>(
                new ResponseObserver.ChangeListener<CompetitionsResponse>() {

                    @Override
                    public void onSuccess(CompetitionsResponse response) {
                        if (response != null && response.getCompetitions() != null) {
                            setCompetitions(response.getCompetitions());
                            setErrorMessage(null);
                        } else setErrorMessage("empty");
                        setIsLoading(false);
                    }

                    @Override
                    public void onException(String errorMessage) {
                        setErrorMessage(errorMessage);
                        setIsLoading(false);
                    }
                })
        );
    }

    private void setCompetitions(List<Competition> competitions) {
        for (int i = 0; i < competitions.size(); i++)
            competitions.get(i).setThemeColor(CompetitionUtils.getColorResourceId(competitions.get(i).getId()));
        mCompetitionList.setValue(competitions);
    }


    public LiveData<Team> getTeam() {
        return mTeam;
    }

    public void setTeam(Team team) {
        mTeam.setValue(team);
    }

    public LiveData<List<Team>> getTeamList() {
        return mTeamList;
    }

    public void fetchTeamList() {
        setIsLoading(true);
        mRepository.fetchTeamList(mCompetition.getValue().getId())
                .subscribe(new ResponseObserver<>(
                        new ResponseObserver.ChangeListener<TeamsResponse>() {

                            @Override
                            public void onSuccess(TeamsResponse response) {
                                if (response != null && response.getTeams() != null) {
                                    setTeams(response.getTeams());
                                    setErrorMessage(null);
                                } else setErrorMessage("empty");
                                setIsLoading(false);
                            }

                            @Override
                            public void onException(String errorMessage) {
                                setErrorMessage(errorMessage);
                                setIsLoading(false);
                            }
                        })
                );
    }

    private void setTeams(List<Team> teams) {
        for (int i = 0; i < teams.size(); i++)
            teams.get(i).setCompetitionId(mCompetition.getValue().getId());
        mTeamList.setValue(teams);
    }

    public LiveData<List<Player>> getPlayerList() {
        return mPlayerList;
    }

    public void fetchTeamInformation() {
        setIsLoading(true);
        mRepository.fetchTeamInformation(mTeam.getValue().getId())
                .subscribe(new ResponseObserver<>(
                        new ResponseObserver.ChangeListener<Team>() {

                            @Override
                            public void onSuccess(Team response) {
                                if (response != null && response.getPlayers() != null) {
                                    mPlayerList.setValue(response.getPlayers());
                                    setErrorMessage(null);
                                } else setErrorMessage("empty");
                                setIsLoading(false);
                            }

                            @Override
                            public void onException(String errorMessage) {
                                setErrorMessage(errorMessage);
                                setIsLoading(false);
                            }
                        })
                );
    }
}
