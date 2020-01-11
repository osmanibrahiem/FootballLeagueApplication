package io.osman.football.league.ui.base;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseBindingActivity<T extends ViewDataBinding, V extends BaseViewModel> extends BaseActivity<V> {

    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private T mViewDataBinding;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    protected abstract int getBindingViewModelVariable();

    @Override
    protected void initViews() {
        performDataBinding();
    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mViewDataBinding.setLifecycleOwner(this);
        if (getViewModel() != null) {
            mViewDataBinding.setVariable(getBindingViewModelVariable(), getViewModel());
            mViewDataBinding.executePendingBindings();
        }
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }
}
