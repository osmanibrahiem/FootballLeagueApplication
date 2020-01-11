package io.osman.football.league.ui.view;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;

import io.osman.football.league.BR;
import io.osman.football.league.R;
import io.osman.football.league.databinding.FragmentTeamsListBinding;
import io.osman.football.league.helper.ColorUtils;
import io.osman.football.league.helper.ConstantUtils;
import io.osman.football.league.model.Competition;
import io.osman.football.league.ui.adapter.TeamsAdapter;
import io.osman.football.league.ui.base.BaseBindingFragment;
import io.osman.football.league.ui.viewmodel.FootballViewModel;

public class TeamsListFragment extends
        BaseBindingFragment<FragmentTeamsListBinding, FootballViewModel> implements
        SwipeRefreshLayout.OnRefreshListener,
        AppBarLayout.OnOffsetChangedListener {

    public static final String FRAGMENT_TAG = "TeamsListFragment";

    private FootballViewModel mViewModel;
    private FragmentTeamsListBinding mBinding;
    private TeamsAdapter mTeamsAdapter;


    public static TeamsListFragment newInstance() {
        return new TeamsListFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_teams_list;
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
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mBinding.recycler.setLayoutManager(layoutManager);
        mBinding.recycler.setHasFixedSize(true);
        mTeamsAdapter = new TeamsAdapter(getBaseActivity())
                .setOnRecyclerItemClickedListener(mViewModel::setTeam);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mBinding.recycler.setAdapter(mTeamsAdapter);

        onRefresh();

        mViewModel.getCompetition().observe(this, competition -> {
            mBinding.toolbar.setTitle(competition.getName());
            setThemeColor(competition);
            mBinding.extraText.setText(competition.getArea().getName() + " | " + competition.getCode());
            if (!TextUtils.isEmpty(competition.getEmblemUrl()))
                ConstantUtils.loadImage(getBaseActivity(), competition.getEmblemUrl(), mBinding.crestImageView);
        });

        mViewModel.getTeamList().observe(this, mTeamsAdapter::setTeams);

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

    @Override
    public void onRefresh() {
        mViewModel.fetchTeamList();
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

    private void setThemeColor(Competition competition) {
        int mainColor = getResources().getColor(competition.getThemeColor()) | 0xFF000000;
        int darkerColor = ColorUtils.getDarkerColor(mainColor, 0.75f);
        mBinding.appBar.setBackground(new ColorDrawable(mainColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getBaseActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(darkerColor);
        }
    }

}
