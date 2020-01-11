package io.osman.football.league.ui.view;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;

import io.osman.football.league.BR;
import io.osman.football.league.R;
import io.osman.football.league.databinding.FragmentTeamInformationBinding;
import io.osman.football.league.helper.ColorUtils;
import io.osman.football.league.helper.CompetitionUtils;
import io.osman.football.league.helper.ConstantUtils;
import io.osman.football.league.ui.adapter.PlayersAdapter;
import io.osman.football.league.ui.base.BaseBindingFragment;
import io.osman.football.league.ui.viewmodel.FootballViewModel;

public class TeamInformationFragment extends
        BaseBindingFragment<FragmentTeamInformationBinding, FootballViewModel> implements
        SwipeRefreshLayout.OnRefreshListener,
        AppBarLayout.OnOffsetChangedListener {

    public static final String FRAGMENT_TAG = "TeamsListFragment";

    private FootballViewModel mViewModel;
    private FragmentTeamInformationBinding mBinding;
    private PlayersAdapter mTeamsAdapter;


    public static TeamInformationFragment newInstance() {
        return new TeamInformationFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_team_information;
    }

    @Override
    public int getBindingViewModelVariable() {
        return BR.viewModel;
    }

    @Override
    public FootballViewModel getViewModel() {
        mViewModel = ViewModelProviders.of(getBaseActivity()).get(FootballViewModel.class);
        return mViewModel;
    }

    @Override
    protected void initActions(@NonNull View view) {
        super.initActions(view);
        mBinding = getViewDataBinding();
        setBackActionBar(mBinding.toolbar);

        mBinding.swipeLayout.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseActivity());
        mBinding.recycler.setLayoutManager(layoutManager);
        mBinding.recycler.setHasFixedSize(true);
        mTeamsAdapter = new PlayersAdapter(getBaseActivity());

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mBinding.recycler.setAdapter(mTeamsAdapter);

        onRefresh();

        mViewModel.getTeam().observe(this, team -> {
            mBinding.setTeam(team);
            setThemeColor(CompetitionUtils.getColorResourceId(team.getCompetitionId()));
            if (!TextUtils.isEmpty(team.getCrestUrl()))
                ConstantUtils.loadImage(getBaseActivity(), team.getCrestUrl(), mBinding.crestImageView);

            mBinding.btnWebSite.setOnClickListener(v -> visitUrl(team.getWebsite()));
        });

        mViewModel.getPlayerList().observe(this, mTeamsAdapter::setPlayers);

        mViewModel.getErrorMessage().observe(this, message -> {
            if (!TextUtils.isEmpty(message)) {
                mBinding.recycler.setVisibility(View.GONE);
                mBinding.errorLayout.rootView.setVisibility(View.VISIBLE);
            } else {
                mBinding.recycler.setVisibility(View.VISIBLE);
                mBinding.errorLayout.rootView.setVisibility(View.GONE);
            }
        });

        mViewModel.isLoading().observe(this, mBinding.swipeLayout::setRefreshing);

        mBinding.errorLayout.retryButton.setOnClickListener(v -> onRefresh());
    }

    private void visitUrl(String website) {
        if (!TextUtils.isEmpty(website)) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(website));
            startActivity(i);
        }
    }

    @Override
    public void onRefresh() {
        mViewModel.fetchTeamInformation();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        mBinding.swipeLayout.setEnabled(i == 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.appBar.addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.appBar.removeOnOffsetChangedListener(this);
    }

    private void setThemeColor(int color) {
        int mainColor = getResources().getColor(color) | 0xFF000000;
        int darkerColor = ColorUtils.getDarkerColor(mainColor, 0.75f);
        mBinding.appBar.setBackground(new ColorDrawable(mainColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getBaseActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(darkerColor);
        }
    }

}
