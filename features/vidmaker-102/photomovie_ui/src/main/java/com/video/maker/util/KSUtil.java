package com.video.maker.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class KSUtil {

    private static final String BLOCKED_FILTER_KEY = "bfk";
    private static final String BLOCKED_TRANSITION_KEY = "btk";
    private static final String BLOCKED_MUSICPOSS_KEY = "bmp";


    private static KSUtil instance;
    private final SharedPreferences sharedPreferences;


    private Set<Integer> filterposs;

    public Set<Integer> getTransitionposs0() {
        return transitionposs0;
    }

    public Set<Integer> getMusicposs() {
        return musicposs;
    }

    private Set<Integer> transitionposs0;
    private Set<Integer> musicposs;

    // Приватный конструктор, чтобы предотвратить создание экземпляров класса
    private KSUtil(Context context) {
        this.filterposs = new HashSet<>();
        this.transitionposs0 = new HashSet<>();
        this.musicposs = new HashSet<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    // Метод для получения единственного экземпляра класса с синхронизацией
    public static synchronized KSUtil getInstance(Context context) {
        if (instance == null) {
            instance = new KSUtil(context);
        }
        return instance;
    }

    public Set<Integer> getFilterposs() {
        return filterposs;
    }

    public void initialize(Set<Integer> data0, Set<Integer> transitionposs0, Set<Integer> musicposs) {
        initFilter(data0);
        initTransition(transitionposs0);
        initMusicposs(musicposs);
    }

    private void initMusicposs(Set<Integer> musicposs) {
        Set<String> stringSet = new HashSet<>();
        for (Integer i : musicposs) {
            if (i != null) {
                stringSet.add(String.valueOf(i));
            }
        }
        Set<String> tmp = sharedPreferences.getStringSet(BLOCKED_MUSICPOSS_KEY, stringSet);
        for (String s : tmp) {
            try {
                this.musicposs.add(Integer.valueOf(s));
            } catch (Exception e) {
                DLog.handleException(e);
            }
        }
    }

    private void initTransition(Set<Integer> data0) {
        Set<String> stringSet = new HashSet<>();
        for (Integer i : data0) {
            if (i != null) {
                stringSet.add(String.valueOf(i));
            }
        }
        Set<String> tmp = sharedPreferences.getStringSet(BLOCKED_TRANSITION_KEY, stringSet);
        for (String s : tmp) {
            try {
                this.transitionposs0.add(Integer.valueOf(s));
            } catch (Exception e) {
                DLog.handleException(e);
            }
        }
    }

    private void initFilter(Set<Integer> data0) {
        Set<String> stringSet = new HashSet<>();
        for (Integer i : data0) {
            if (i != null) {
                stringSet.add(String.valueOf(i));
            }
        }
        Set<String> tmp = sharedPreferences.getStringSet(BLOCKED_FILTER_KEY, stringSet);
        for (String s : tmp) {
            try {
                filterposs.add(Integer.valueOf(s));
            } catch (Exception e) {
                DLog.handleException(e);
            }
        }
    }



//    public void clearAll() {
//        getTransitionposs0().clear();
//        musicposs.clear();
//        filterposs.clear();
//    }

    //====
    public void unlockFilterItem(int position) {
        this.filterposs.remove(position);
        saveUnlockedFilterItems();
    }

    public boolean isFilterItemLocked(int position) {
        return this.filterposs.contains(position);
    }

    public void saveUnlockedFilterItems() {
        Set<String> stringSet = new HashSet<>();
        for (Integer item : filterposs) {
            if (item != null) {
                stringSet.add(String.valueOf(item));
            }
        }
        sharedPreferences.edit().putStringSet(BLOCKED_FILTER_KEY, stringSet).apply();
    }
    //====
    public void unlockTransitionpossItem(int position) {
        this.transitionposs0.remove(position);
        saveUnlockedTransitionpossItems();
    }

    public boolean isTransitionpossItemLocked(int position) {
        return this.transitionposs0.contains(position);
    }

    public void saveUnlockedTransitionpossItems() {
        Set<String> stringSet = new HashSet<>();
        for (Integer item : transitionposs0) {
            if (item != null) {
                stringSet.add(String.valueOf(item));
            }
        }
        sharedPreferences.edit().putStringSet(BLOCKED_TRANSITION_KEY, stringSet).apply();
    }

    //====
    public void unlockMusicpossItem(int position) {
        this.musicposs.remove(position);
        saveUnlockedMusicpossItems();
    }

    public boolean isMusicpossItemLocked(int position) {
        return this.musicposs.contains(position);
    }

    public void saveUnlockedMusicpossItems() {
        Set<String> stringSet = new HashSet<>();
        for (Integer item : musicposs) {
            if (item != null) {
                stringSet.add(String.valueOf(item));
            }
        }
        sharedPreferences.edit().putStringSet(BLOCKED_MUSICPOSS_KEY, stringSet).apply();
    }

    //====
    //====

    //====


}
