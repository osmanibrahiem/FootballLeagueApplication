package io.osman.football.league.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import io.osman.football.league.R;
import io.osman.football.league.helper.NetworkUtils;

public abstract class BaseActivity<V extends BaseViewModel> extends AppCompatActivity {

    private V mViewModel;

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    protected V getViewModel() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preInit();
        initViews();
        if (getViewModel() != null) {
            getViewModel().setBaseActivity(this);
        }
        initActions();
    }

    protected void preInit() {
    }

    protected void initViews() {
        setContentView(getLayoutId());
    }

    protected void initActions() {
    }

    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
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
        FragmentTransaction transaction = getSupportFragmentManager()
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

    public void removeFragment(@NonNull Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                R.anim.slide_in_left, R.anim.slide_out_right);

        transaction.remove(fragment);
        transaction.commit();
    }

    public void addFragment(@NonNull Fragment fragment,
                            @NonNull String tag, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        if (addToBackStack) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right);
        }

        transaction.add(fragment, tag);

        if (addToBackStack)
            transaction.addToBackStack(null);

        transaction.commit();

    }

    public Fragment findFragment(@NonNull String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public Fragment findFragment(@IdRes int id) {
        return getSupportFragmentManager().findFragmentById(id);
    }

    public void startActivity(Class activity) {
        startActivity(new Intent(this, activity));
    }

    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public float convertDpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
}
