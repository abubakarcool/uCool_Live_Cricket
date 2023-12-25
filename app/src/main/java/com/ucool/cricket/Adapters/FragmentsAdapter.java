package com.example.samplestickerapp.Adapters;
//FragmentPagerAdapter:
// This adapter is best for use when there are a limited number of fragments that need to be paged, as it keeps all fragments in memory.
//When the fragment is no longer visible to the user, it's still kept fully intact in the memory.
//Useful when the overhead of initializing and maintaining fragments is not significant, meaning you don't have a lot of fragments or they are lightweight.
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.samplestickerapp.Fragments.CallsFragment;
import com.example.samplestickerapp.Fragments.ChatsFragment;
import com.example.samplestickerapp.Fragments.FixturesFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 : return new FixturesFragment();
            case 1 : return new ChatsFragment();
            case 2 : return new CallsFragment();
            default: return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position==0){
            title = "Fixtures";
        }
        if(position==1){
            title = "Live TV";
        }
        if(position==2){
            title = "Table";
        }
        return title;
    }
}
