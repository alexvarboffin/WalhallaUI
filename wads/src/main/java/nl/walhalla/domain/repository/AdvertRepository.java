package nl.walhalla.domain.repository;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdView;

/**
 * Created by combo on 18.07.2017.
 */

public interface AdvertRepository {
    AdView getNewAdsBanner(ViewGroup viewGroup);

   // void initialize(Context context);
}
