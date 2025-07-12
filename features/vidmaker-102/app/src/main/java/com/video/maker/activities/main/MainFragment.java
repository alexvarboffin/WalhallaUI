package com.video.maker.activities.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.video.maker.activities.pickedimages.PickedImagesActivity;
import com.video.maker.activities.PrivacyActivity;
import com.video.maker.activity.ShareActivity;

import com.video.maker.dialog.PrmsnDialog;
import com.video.maker.mycreation.MyVideoAdapter;
import com.video.maker.util.KSUtil;
import com.video.makerpro.R;
import com.video.makerpro.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment implements View.OnClickListener {

    private long mLastClickTime = 0;

    public static ArrayList<String> videoPath = new ArrayList<String>();
    MyVideoAdapter videoAdapter;
    int FLAG_VIDEO = 21;
    
    private @NonNull ActivityMainBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        addControls();
        videoLoader();
        
        //MAX + Fb bidding Ads
//        AdManager.initAD(MainFragment.this);
//        AdManager.LoadInterstitalAd(MainFragment.this);
//        AdManager.createNativeAdMAX(MainFragment.this, rl_native_ad);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void videoLoader() {
        getFromStorage();
        Log.e("videoLoader: ", "" + videoPath);
        videoAdapter = new MyVideoAdapter(videoPath, getActivity(), (v, position) -> {

            Intent intent;
            File file = new File(videoPath.get(position));
            if (new File(videoPath.get(position)).getAbsolutePath().contains(".mp4")) {
                intent = ShareActivity.newIntent(getActivity(), file, false);
            } else {
                intent = ShareActivity.newIntent(getActivity(), file, true);
            }
            startActivity(intent);
        });

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(videoAdapter);

    }


    public void getFromStorage() {
        String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/VideoMaker";
        File file = new File(folder);
        videoPath = new ArrayList<String>();
        if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            Arrays.sort(listFile, (o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));
            for (int i = 0; i < listFile.length; i++) {
                videoPath.add(listFile[i].getAbsolutePath());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLAG_VIDEO) {
            videoAdapter.notifyDataSetChanged();
            videoLoader();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        videoLoader();
    }

    private void addControls() {
        binding.btNewProject.setOnClickListener(this);
        binding.btRate.setOnClickListener(this);
        binding.btShare.setOnClickListener(this);
        //binding.po.setOnClickListener(this);
        binding.btPrivacy.setOnClickListener(this);
        binding.btMore.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        String packageName = getActivity().getPackageName();
        if (id == R.id.bt_new_project) {
            KSUtil.getInstance().clearAll();
            if (SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {
                this.mLastClickTime = SystemClock.elapsedRealtime();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    Dexter.withContext(getContext()).withPermissions("android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO", "android.permission.READ_MEDIA_AUDIO").withListener(new MultiplePermissionsListener() {
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                picker();
                            }
                            if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                PrmsnDialog.showSettingDialog(getActivity());
                            }
                        }

                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).withErrorListener(new PermissionRequestErrorListener() {
                        public void onError(DexterError dexterError) {
                            Toast.makeText(getActivity().getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                        }
                    }).onSameThread().check();

                    return;
                } else {
                    Dexter.withContext(getActivity()).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                picker();
                            }
                            if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                PrmsnDialog.showSettingDialog(getActivity());
                            }
                        }

                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).withErrorListener(dexterError -> Toast.makeText(getActivity().getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
                }
                return;
            }
            return;


//            case R.id.bt_policy:
//                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.policy_url))));
//                return;
        } else if (id == R.id.bt_rate) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="
                    + packageName)));
            return;
        } else if (id == R.id.bt_privacy) {
            startActivity(new Intent(getActivity(), PrivacyActivity.class));
            return;
        } else if (id == R.id.bt_more) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7081479513420377164&hl=en")));
            return;
        } else if (id == R.id.bt_share) {
            try {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "My application name");
                intent.putExtra("android.intent.extra.TEXT", "\nLet me recommend you this application\n\n" + "\"https://play.google.com/store/apps/details?id="
                        + packageName);
                startActivity(Intent.createChooser(intent, "Choose one"));
                return;
            } catch (Exception unused) {
                return;
            }
        }
        return;
    }

    private void picker() {
        Intent m = new Intent(getActivity(), PickedImagesActivity.class);
        MainFragment.this.startActivity(m);
    }


    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }


}
