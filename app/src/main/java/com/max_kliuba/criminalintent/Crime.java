package com.max_kliuba.criminalintent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Crime {
    public static final int ORDINARY_CRIME = 0;
    public static final int SERIOUS_CRIME = 1;

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;
    private int mCrimeType;

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public String getFormattedTime() {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("HH:mm", Locale.US);
        return simpleDateFormat.format(mDate);
    }

    public String getFormattedDate() {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.US);
        return simpleDateFormat.format(mDate);
    }

    public String getFormattedDateTime() {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("HH:mm, EEEE, MMM dd, yyyy", Locale.US);
        return simpleDateFormat.format(mDate);
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public int getCrimeType() {
        return mCrimeType;
    }

    public void setCrimeType(int crimeType) {
        mCrimeType = crimeType;
    }
}
