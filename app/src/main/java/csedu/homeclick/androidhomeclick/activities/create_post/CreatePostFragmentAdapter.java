package csedu.homeclick.androidhomeclick.activities.create_post;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class CreatePostFragmentAdapter extends FragmentStateAdapter {
    public CreatePostFragmentAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CreateRentPostFragment();
            case 1:
                return new CreateSalePostFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
