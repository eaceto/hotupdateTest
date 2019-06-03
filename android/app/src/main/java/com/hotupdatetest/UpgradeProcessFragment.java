package com.hotupdatetest;


import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hotupdatetest.constants.FileConstants;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpgradeProcessFragment extends Fragment {
    private static String TAG = "saul";


    public UpgradeProcessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "upgradeProcessFragment");

        View view = inflater.inflate(R.layout.fragment_upgrade_process, container, false);
        return view;
    }


}
