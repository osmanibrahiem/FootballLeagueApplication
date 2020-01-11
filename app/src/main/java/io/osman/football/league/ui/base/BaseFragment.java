package io.osman.football.league.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import io.osman.football.league.R;

public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    private BaseActivity mActivity;

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initViews(inflater, container);
    }

    protected View initViews(@NonNull LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initActions(view);
    }

    protected void initActions(@NonNull View view) {
    }

    @Nullable
    public final <T extends View> T findViewById(@IdRes int id) {
        if (mRootView == null) {
            return null;
        }
        return mRootView.findViewById(id);
    }

    public void setSupportActionBar(Toolbar toolbar) {
        if (mActivity != null && toolbar != null)
            mActivity.setSupportActionBar(toolbar);
    }

    public ActionBar getSupportActionBar() {
        if (mActivity != null)
            return mActivity.getSupportActionBar();
        return null;
    }

    public void setBackActionBar(Toolbar toolbar) {
        setBackActionBar(toolbar, v -> onBackPressed());
    }

    public void setBackActionBar(Toolbar toolbar, View.OnClickListener listener) {
        setBackActionBar(toolbar, 0, listener);
    }

    public void setBackActionBar(Toolbar toolbar, @DrawableRes int icon, View.OnClickListener listener) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (icon != 0)
                    getSupportActionBar().setHomeAsUpIndicator(icon);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                if (listener != null)
                    toolbar.setNavigationOnClickListener(listener);
            }
        }
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    public void onBackPressed() {
        if (mActivity != null)
            mActivity.onBackPressed();
    }

    public void replaceFragment(@IdRes int containerViewId, @NonNull Fragment fragment) {
        replaceFragment(containerViewId, fragment, false);
    }

    public void replaceFragment(@IdRes int containerViewId, @NonNull Fragment fragment, boolean addToBackStack) {
        replaceFragment(containerViewId, fragment, fragment.getClass().getSimpleName(), addToBackStack);
    }

    public void replaceFragment(@IdRes int containerViewId, @NonNull Fragment fragment,
                                @NonNull String tag) {
        replaceFragment(containerViewId, fragment, tag, false);
    }

    public void replaceFragment(@IdRes int containerViewId, @NonNull Fragment fragment,
                                @NonNull String tag, boolean addToBackStack) {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();

        if (addToBackStack) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right);
        }

        transaction.replace(containerViewId, fragment, tag);

        if (addToBackStack)
            transaction.addToBackStack(String.valueOf(containerViewId));

        transaction.commit();

    }

    public Fragment findFragment(@NonNull String tag) {
        return getChildFragmentManager().findFragmentByTag(tag);
    }

    public Fragment findFragment(@IdRes int id) {
        return getChildFragmentManager().findFragmentById(id);
    }

    public void startActivity(Class activity) {
        if (mActivity != null) {
            mActivity.startActivity(activity);
        }
    }

    public void startActivity(Intent intent) {
        if (mActivity != null) {
            mActivity.startActivity(intent);
        }
    }

    public float convertDpToPx(float dp) {
        if (mActivity != null) {
            return mActivity.convertDpToPx(dp);
        }
        return 0;
    }
}
