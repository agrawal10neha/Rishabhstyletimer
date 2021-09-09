package com.si.styletimer.utill;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.si.styletimer.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Argalon-PC on 14-03-2018.
 */

public class CameraGallery {
    private static final String TAG = "CameraGallery";

    public final static int REQUEST_CAMERA_IMAGE = 1034;
    private static final int REQUEST_GALLERY_IMAGE = 1063;
    private File photoFile;
    private Uri resultUri;
    private Context context;
    private MarshmallowPermission marshmallowPermission;
    private Activity activity;
    private File saveFile;
    String imageFilePath;

    public CameraGallery(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.marshmallowPermission = new MarshmallowPermission(context);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (permissions.length == 0) {
            return;
        }
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    //denied
                    Log.e("denied", permission);
                } else {
                    if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", permission);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage(context.getString(R.string.permission_message))

                        .setPositiveButton(context.getString(R.string.settings), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // CameraIntentTestActivity.this.finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        } else {

            switch (requestCode) {
                case MarshmallowPermission.REQUEST_CAMERA_PERMISSION:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        onLaunchCamera();
                    }
                    break;
                case MarshmallowPermission.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        pickFromGallery();
                    }
                    break;

                case MarshmallowPermission.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION:

                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        saveCroppedImage();
                    }
            }
        }

    }

    public void onLaunchCamera() {

        if (marshmallowPermission.check_camera_Permission()){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = getPhotoFileUri();

            Uri fileProvider = FileProvider.getUriForFile(activity, context.getString(R.string.file_provider_authorities), photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                activity.startActivityForResult(intent, REQUEST_CAMERA_IMAGE);

            }

            //  openCameraIntent();
        }

    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(context.getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(activity, context.getString(R.string.file_provider_authorities), photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(pictureIntent, REQUEST_CAMERA_IMAGE);
            }
        }
    }


    public void pickFromGallery() {

        if(marshmallowPermission.check_read_external_storage_permission()){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(galleryIntent, REQUEST_GALLERY_IMAGE);
        }

       /* if (marshmallowPermission.check_read_external_storage_permission()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            activity.startActivityForResult(Intent.createChooser(intent, context.getString(R.string.label_select_picture)), REQUEST_GALLERY_IMAGE);
        }*/
    }


    private File getPhotoFileUri() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = timeStamp +context.getString(R.string.photo_jpg);

        /*Use this if you did not want to repeat images*/
        String photoFileName = "photo.jpg";

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));
        // File mediaStorageDir = new File(getCacheDir(),getString(R.string.app_name));

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.e(TAG, "failed to create directory");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + imageFileName);

        return file;

    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri camerafile = Uri.fromFile(photoFile);

                startCropActivity(camerafile);
            } else {
                Toast.makeText(activity, context.getString(R.string.pic_not_taken_error), Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUEST_GALLERY_IMAGE){
            if (resultCode == RESULT_OK) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(selectedUri);
                } else {
                    Toast.makeText(activity, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == UCrop.REQUEST_CROP){
            if (resultCode == UCrop.RESULT_ERROR){
                handleCropError(data);
            }else {
                if (data!=null){
                    handleCropResult(data);
                }

            }

        }

        /*if (requestCode == REQUEST_CAMERA_IMAGE) {
            //don't compare the data to null, it will always come as  null because we are providing a file URI, so load with the imageFilePath we obtained before opening the cameraIntent
            Log.e(TAG, "onActivityResult: "+imageFilePath );
            // If you are using Glide.

            Uri camerafile = Uri.parse(imageFilePath);
            startCropActivity(camerafile);
        }*/


    }


    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(context.getCacheDir(), destinationFileName)));
        uCrop = uCrop.useSourceImageAspectRatio();
        uCrop = advancedConfig(uCrop);
        uCrop.start(activity);

    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(true);
        options.setToolbarTitle("Bild bearbeiten");
        options.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));

        /*Use this code if you want to crop a circle image*/
        //options.setCircleDimmedLayer(true);

        return uCrop.withOptions(options);
    }


    private void handleCropResult(@NonNull Intent result) {
        resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            File dsf = new File(String.valueOf(resultUri));
            Log.e(TAG, "handleCropResult: "+getReadableFileSize(dsf.length()));
            saveCroppedImage();
        } else {
            Toast.makeText(context, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(context, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCroppedImage() {

        if (marshmallowPermission.check_write_Permission()){
            try {
                copyFileToDownloads(resultUri);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private void copyFileToDownloads(Uri croppedFileUri) throws Exception {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = timeStamp +context.getString(R.string.photo_jpg);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),context. getString(R.string.app_name));

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.e(TAG, "failed to create directory");
        }

        saveFile = new File(mediaStorageDir, imageFileName);

        File dfsdf = new File(String.valueOf(saveFile));

        FileInputStream inStream = new FileInputStream(new File(croppedFileUri.getPath()));
        FileOutputStream outStream = new FileOutputStream(saveFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();

        Log.e(TAG, "file Storage: "+saveFile );
        Log.e(TAG, "file Storage: "+getReadableFileSize(saveFile.length()));

    }

    public File outputImageFile(){
        return saveFile;
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
