package csedu.homeclick.androidhomeclick.activities.filter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import csedu.homeclick.androidhomeclick.activities.sign_in_fragments.SignInFragment;
import csedu.homeclick.androidhomeclick.activities.sign_in_fragments.SignUpFragment;

public class FilterFragmentAdapter extends FragmentStateAdapter {
    public FilterFragmentAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FilterRentFragment();
            case 1:
                return new FilterBuyFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
