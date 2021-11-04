package com.mristudio.blooddonation.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mristudio.blooddonation.view.fragment.ContactsFragment;
import com.mristudio.blooddonation.view.fragment.MyChatsFragment;

public class MessageViewPagerAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MessageViewPagerAdapter(Context myContext, FragmentManager fm, int totalTabs) {
        super(fm);
        this.myContext = myContext;
        this.totalTabs = totalTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyChatsFragment myChatsFragment = new MyChatsFragment();
                return myChatsFragment;
            case 1:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
