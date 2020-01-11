package io.osman.football.league.ui.view;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import io.osman.football.league.R;
import io.osman.football.league.ui.base.BaseActivity;
import io.osman.football.league.ui.viewmodel.FootballViewModel;

public class MainActivity extends BaseActivity<FootballViewModel> {

    private FootballViewModel mViewModel;

    @Override
    public int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected FootballViewModel getViewModel() {
        mViewModel = ViewModelProviders.of(this).get(FootballViewModel.class);
        return mViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CompetitionsListFragment.newInstance())
                    .commitNow();
        }

        mViewModel.getCompetition().observe(this, competition ->
                replaceFragment(R.id.container, TeamsListFragment.newInstance(), true));

        mViewModel.getTeam().observe(this, team ->
                replaceFragment(R.id.container, TeamInformationFragment.newInstance(), true));
    }
}
