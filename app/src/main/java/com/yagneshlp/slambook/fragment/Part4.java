package com.yagneshlp.slambook.fragment;
//TODO: Evict all the code and make a better replacemnt for photo upload

import android.Manifest;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.activity.SlambookActivity;
import com.yagneshlp.slambook.helper.RealPathUtil;
import com.yagneshlp.slambook.helper.SessionManager;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Part4 extends Fragment {


    public Part4() {
        // Required empty public constructor
    }

    //Declaring views
    private Button buttonChoose;
    private ActionProcessButton  buttonUpload;
    private ImageView imageView;
    private EditText editText;
    private TextView text;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_part4, container, false);

        requestStoragePermission();

        //Initializing views
        buttonChoose = (Button) view.findViewById(R.id.buttonChoose);
        buttonUpload = (ActionProcessButton) view.findViewById(R.id.buttonUpload);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        editText = (EditText) view.findViewById(R.id.editTextName);
        text = (TextView)  view.findViewById(R.id.textSelf);

        buttonUpload.setMode(ActionProcessButton.Mode.ENDLESS);

        //Setting clicklistener
        buttonChoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showFileChooser();

            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                uploadMultipart();

            }
        });


        return view;
    }


    /*
  * This is the method responsible for image upload
  * We need the full image path and the name for the image in this method
  * */
    public void uploadMultipart() {

        SessionManager cur = new SessionManager(getContext());
        //getting name for the image
        String name = cur.getUsername();

        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getContext(), uploadId, com.yagneshlp.slambook.src.Constants.UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(3)

                    .startUpload(); //Starting the upload

            Toast.makeText(getContext(),
                    "Photo Upload Started, Check Notification for Status.", Toast.LENGTH_LONG).show();
            SlambookActivity.viewPager.setCurrentItem(SlambookActivity.viewPager.getCurrentItem()+1,true);


        } catch (Exception exc) {
            Toast.makeText(getContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                text.setText("You Look Great ;)");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {

        String path = null;

        if (Build.VERSION.SDK_INT < 11) {
            path = RealPathUtil.getRealPathFromURI_BelowAPI11(getActivity(), uri);
        } else if (Build.VERSION.SDK_INT < 19) {
            path = RealPathUtil.getRealPathFromURI_API11to18(getActivity(), uri);
        } else {
            path = RealPathUtil.getRealPathFromURI_API19(getActivity(), uri);
        }

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }





}

