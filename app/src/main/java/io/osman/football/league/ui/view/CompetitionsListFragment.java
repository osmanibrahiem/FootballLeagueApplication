package io.osman.football.league.ui.view;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;

import io.osman.football.league.BR;
import io.osman.football.league.R;
import io.osman.football.league.databinding.FragmentCompetitionsListBinding;
import io.osman.football.league.ui.adapter.CompetitionsAdapter;
import io.osman.football.league.ui.base.BaseBindingFragment;
import io.osman.football.league.ui.viewmodel.FootballViewModel;

public class CompetitionsListFragment extends
        BaseBindingFragment<FragmentCompetitionsListBinding, FootballViewModel> implements
        SwipeRefreshLayout.OnRefreshListener,
        AppBarLayout.OnOffsetChangedListener {

    public static final String FRAGMENT_TAG = "CompetitionsListFragment";

    private FootballViewModel mViewModel;
    private FragmentCompetitionsListBinding mBinding;
    private CompetitionsAdapter mCompetitionAdapter;


    public static CompetitionsListFragment newInstance() {
        return new CompetitionsListFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_competitions_list;
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

        mBinding.swipeLayout.setOnRefreshListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mBinding.recycler.setLayoutManager(layoutManager);
        mBinding.recycler.setHasFixedSize(true);
        mCompetitionAdapter = new CompetitionsAdapter(getBaseActivity())
                .setOnRecyclerItemClickedListener(mViewModel::setCompetition);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mBinding.recycler.setAdapter(mCompetitionAdapter);

        onRefresh();

        mViewModel.getCompetitionList().observe(this, mCompetitionAdapter::setCompetitions);

        mViewModel.getErrorMessage().observe(this, message -> {
            if (TextUtils.isEmpty(message)) {
                mBinding.recycler.setVisibility(View.VISIBLE);
                mBinding.errorLayout.rootView.setVisibility(View.GONE);
            } else {
                mBinding.recycler.setVisibility(View.GONE);
                mBinding.errorLayout.rootView.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.isLoading().observe(this, mBinding.swipeLayout::setRefreshing);

        mBinding.errorLayout.retryButton.setOnClickListener(v -> onRefresh());
    }

    @Override
    public void onRefresh() {
        mViewModel.fetchCompetitionList();
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
}
