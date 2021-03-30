package com.example.mymusicplayer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Date;

public class AddSongFragment extends Fragment {

    // Permission constants
    private static final int WRITE_EXTERNAL_STORAGE_CAMERA_PERMISSION_REQUEST_CODE = 0;
    private static final int WRITE_EXTERNAL_STORAGE_GALLERY_PERMISSION_REQUEST_CODE = 1;

    // Activity result request codes constants
    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int GALLERY_REQUEST_CODE = 1;

    private static final String AUTHORITY = "com.example.mymusicplayer.fileprovider";

    private static final String TAG = "AddSongFragment";

    private File imageFile;

    private final Song song = new Song();

    private ImageView pictureIv;
    private EditText songNameEt;
    private EditText artistNameEt;
    private EditText songLinkEt;
    private Button addSongToPlaylistBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_song, container, false);

        pictureIv = rootView.findViewById(R.id.picture_iv);
        songNameEt = rootView.findViewById(R.id.song_name_et);
        artistNameEt = rootView.findViewById(R.id.artist_name_et);
        songLinkEt = rootView.findViewById(R.id.song_link_et);

        FloatingActionButton addPictureFab = rootView.findViewById(R.id.add_picture_fab);
        addPictureFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddImageDialog();
            }
        });

        addSongToPlaylistBtn = rootView.findViewById(R.id.add_song_to_playlist_btn);
        addSongToPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSongToPlaylist();
            }
        });

        return rootView;
    }

    private void showAddImageDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Methods to add picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    captureImageFromCameraConfirmPermission();
                } else if (options[item].equals("Choose from Gallery")) {
                    pickImageFromGalleryConfirmPermission();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void captureImageFromCameraConfirmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasWritePermission = getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (hasWritePermission == PackageManager.PERMISSION_GRANTED) {
                captureImageFromCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CAMERA_PERMISSION_REQUEST_CODE);
            }
        } else {
            captureImageFromCamera();
        }
    }

    private void pickImageFromGalleryConfirmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasWritePermission = getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (hasWritePermission == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_GALLERY_PERMISSION_REQUEST_CODE);
            }
        } else {
            pickImageFromGallery();
        }
    }

    private void captureImageFromCamera() {
        imageFile = new File(getActivity().getExternalFilesDir(null), new Date().getTime() + "pic.jpg");

        Uri imageUri = FileProvider.getUriForFile(getContext(), AUTHORITY, imageFile);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
    }

    private void pickImageFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GALLERY_REQUEST_CODE);
    }

    private void addSongToPlaylist() {
        String songName = songNameEt.getText().toString();
        String artistName = artistNameEt.getText().toString();
        String songLink = songLinkEt.getText().toString();

        NavDirections action;
        if (songName.isEmpty() || artistName.isEmpty() || songLink.isEmpty()) {
            showErrorDialog();
        } else { // all fields are filled
            song.setSongName(songName);
            song.setArtistName(artistName);
            song.setSongLink(songLink);

            MainPlayerFragment.songs.add(song);
            MyFileUtils.saveObjectToFile(getContext(), MainPlayerFragment.SONGS_FILE_KEY, MainPlayerFragment.songs);

            action = AddSongFragmentDirections.navigateToMainPlayerFragment();
            Navigation.findNavController(addSongToPlaylistBtn).navigate(action);
        }

    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_DARK);

        builder.setIcon(R.drawable.ic_baseline_error_outline_24)
                .setTitle("Wrong input")
                .setMessage("All fields must be filled")
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Glide.with(getContext()).load(imageFile.getAbsolutePath()).into(pictureIv);
            song.setSongImagePath(imageFile.getAbsolutePath());
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            Glide.with(getContext()).load(imageUri.toString()).into(pictureIv);
            song.setSongImagePath(imageUri.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (requestCode == WRITE_EXTERNAL_STORAGE_CAMERA_PERMISSION_REQUEST_CODE && hasPermission) {
            captureImageFromCamera();
        } else if (requestCode == WRITE_EXTERNAL_STORAGE_GALLERY_PERMISSION_REQUEST_CODE && hasPermission) {
            pickImageFromGallery();
        } else {
            Toast.makeText(getContext(), "Need permission to add image", Toast.LENGTH_SHORT).show();
        }
    }
}
