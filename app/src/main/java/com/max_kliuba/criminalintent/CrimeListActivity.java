package com.max_kliuba.criminalintent;

import android.content.Intent;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    private Fragment mDetailFragment;

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            mDetailFragment = null;

            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            mDetailFragment = CrimeFragment.newInstance(crime.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, mDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onCrimeFromListDeleted() {
        removeDetailFragment();
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUserInterface();
    }

    @Override
    public void onCrimeDeleted() {
        removeDetailFragment();
    }

    private void removeDetailFragment() {
        if (mDetailFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mDetailFragment).commit();
        }
    }
}
