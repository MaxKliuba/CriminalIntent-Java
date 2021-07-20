package com.max_kliuba.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class PhotoDialogFragment extends DialogFragment {

    private static final String ARG_PHOTO_PATH = "photo_path";
    private static final String PROVIDER_AUTHORITY = "com.max_kliuba.criminalintent.fileprovider";
    private static final int REQUEST_PHOTO = 0;

    private String mPhotoPath;
    private File mPhotoFile;
    private ImageView mPhotoView;
    private Button mPhotoButton;

    public static PhotoDialogFragment newInstance(String photoFilePath) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_PATH, photoFilePath);

        PhotoDialogFragment fragment = new PhotoDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mPhotoPath = (String) getArguments().get(ARG_PHOTO_PATH);
        mPhotoFile = new File(mPhotoPath);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);

        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        ViewTreeObserver observer = mPhotoView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updatePhotoView();
            }
        });

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null
                && captureImage.resolveActivity(packageManager) != null;

        mPhotoButton = (Button) v.findViewById(R.id.photo_button);
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), PROVIDER_AUTHORITY, mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager()
                        .queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(v).create();
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), PROVIDER_AUTHORITY, mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    private void updatePhotoView() {
        Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoPath, mPhotoView);
        mPhotoView.setImageBitmap(bitmap);
    }
}
