package com.yagneshlp.slambook.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dd.processbutton.iml.ActionProcessButton;
import com.master.permissionhelper.PermissionHelper;
import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.activity.SlambookActivity;
import com.yagneshlp.slambook.app.AppConfig;
import com.yagneshlp.slambook.app.AppController;
import com.yagneshlp.slambook.helper.AndroidMultiPartEntity;
import com.yagneshlp.slambook.helper.SQLiteHandler;
import com.yagneshlp.slambook.helper.SessionManager;
import com.yagneshlp.slambook.src.Config;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import com.yagneshlp.slambook.helper.AndroidMultiPartEntity.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Part4 extends Fragment {


    public Part4() {
        // Required empty public constructor
    }

    //Declaring views
    private Button buttonChoose;
    private ActionProcessButton buttonUpload;
    private ImageView imageView;
    PermissionHelper permissionHelper;
    private TextView text;
    private String filePath = null;
    long totalSize = 0;


    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
   // private Uri filePath;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri;

    private static final String TAG = SlambookActivity.class.getSimpleName(); //for Logger Purposes

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_part4, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //Initializing views
        buttonChoose = (Button) view.findViewById(R.id.buttonChoose);
        buttonUpload = (ActionProcessButton) view.findViewById(R.id.buttonUpload);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        text = (TextView)  view.findViewById(R.id.textSelf);

        permissionHelper = new PermissionHelper(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Log.d(TAG, "onPermissionGranted() called");
            }

            @Override
            public void onPermissionDenied() {
                Log.d(TAG, "onPermissionDenied() called");
            }

            @Override
            public void onPermissionDeniedBySystem() {
                Log.d(TAG, "onPermissionDeniedBySystem() called");
            }
        });


       // buttonUpload.setMode(ActionProcessButton.Mode.ENDLESS);

        //Setting clicklistener
        buttonChoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                captureImage();

            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


            }
        });

        if (!isDeviceSupportCamera()) {
            Toast.makeText(getContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            getActivity().finish();
        }





        return view;
    }

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = null;

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);



        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Launching camera app to record video
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }




    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void launchUploadActivity(boolean isImage){
       // Intent i = new Intent(MainActivity.this, UploadActivity.class);
        //i.putExtra("filePath", fileUri.getPath());
        //i.putExtra("isImage", isImage);
       // startActivity(i);
        filePath=fileUri.getPath();
        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        buttonUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // uploading the file to server
                //new UploadFileToServer().execute();
                checker();
            }
        });
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imageView.setVisibility(View.VISIBLE);
            //vidPreview.setVisibility(View.GONE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imageView.setImageBitmap(bitmap);
            text.setText("You Look great ;) ");
        }
    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            buttonUpload.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            //buttonUpload.setVisibility(View.VISIBLE);

            // updating progress bar value
            buttonUpload.setProgress(progress[0]);

            // updating percentage value
           // txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("yagnesh.lp"));
                entity.addPart("email", new StringBody("yagneshlp@gmail.com"));
                SessionManager cur = new SessionManager(getContext());  //object of the session manager
                String uname=cur.getUsername(); //getting current user name from sessionmnager
                entity.addPart("username",new StringBody(uname));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            //showAlert(result);
            try{
            JSONObject jObj = new JSONObject(result);
            boolean error = jObj.getBoolean("error");
                if(!error)
                {
                    getSet_progress();
                }
                else
                {
                    Log.d(TAG,"Error Recieved On Photo Upload: " + result.toString() );
                    Toast.makeText(getContext(),"Internal Error Occured, Try again Later",Toast.LENGTH_LONG);
                }

            }
            catch (JSONException e)
            {
                Log.e(TAG,"JSON not recieved On Photo Upload: " + result.toString() );
            }


            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void checker()
    {
        String tag_string_req = "req_page4_val";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Page 4 get Response: " + response.toString());

                try {
                    final JSONObject jObj = new JSONObject(response); //objectifying the json
                    boolean error = jObj.getBoolean("error");  //detecting if an error was sent in json
                    // Check for error node in json
                    if (!error) {
                        //Got status Successfully
                        String status=jObj.getString("value");
                        if(status.equals("Yes"))
                        {

                            new android.support.v7.app.AlertDialog.Builder(getContext())
                                    .setTitle("Update the Data?")
                                    .setMessage("This page has already been filled.\nDo you want to update it with current data or retain previous data?")
                                    .setPositiveButton("Retain old info", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SlambookActivity.viewPager.setCurrentItem(SlambookActivity.viewPager.getCurrentItem()+1,true); //the veiwpager is changed to next page
                                            Log.d(TAG,"User decided to retain old value" ); //logging the error message
                                        }
                                    })
                                    .setNegativeButton("Update with new data", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new UploadFileToServer().execute();
                                        }
                                    })
                                    .show();
                        }
                        else
                            new UploadFileToServer().execute();

                    } else {
                        // Error in Submission
                        buttonUpload.setProgress(-1); //button is set at error colour - red
                        String errorMsg = jObj.getString("message"); //extracting the error
                        Log.d(TAG,"Message returned from server: " + errorMsg ); //logging the error message
                        Toast.makeText(getContext(),"An Error occured and logged, try Again", Toast.LENGTH_LONG).show(); //displaying an error to the user
                    }
                } catch (JSONException e) {
                    // JSON data was not returned, because an error at php script/mysql
                   // button.setProgress(-1);
                    e.printStackTrace(); //logging error
                   // Toast.makeText(getContext(), "Internal error occured, try again later", Toast.LENGTH_LONG).show(); //displaying an error to the user
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                buttonUpload.setProgress(-1);
                Log.e(TAG, "Volley Error: " + error.getMessage()); //error in android part logged
                Toast.makeText(getContext(),"Local error, try again", Toast.LENGTH_LONG).show(); //displaying the error to user
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                SQLiteHandler db= new SQLiteHandler(getContext());  //object of the sqlLite helper
                SessionManager cur = new SessionManager(getContext());  //object of the session manager
                String uid=db.getUserID();  //getting the current userid from local db
                String uname=cur.getUsername(); //getting current user name from sessionmnager
                Map<String, String> params = new HashMap<String, String>();
                params.put("route", "4");               //   json POST paran add
                params.put("userid",uid);               //   "
                params.put("username", uname);          //    "
                params.put("need", "get");             //    "
                return params;  //returning ready json
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getSet_progress() {
        // Tag used to cancel the request
        String tag_string_req = "req_page4_sub";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Page 4 Submit Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        //Submitted Successfully
                        //button.setProgress(100); //the button is set to green colour(subitted)
                        SlambookActivity.viewPager.setCurrentItem(SlambookActivity.viewPager.getCurrentItem()+1,true); //the veiwpager is changed to next page
                        String errorMsg = jObj.getString("message"); //extracting the message
                        Log.d(TAG,"Message returned from server: " + errorMsg ); //logging the error message
                        SessionManager cur = new SessionManager(getContext()); //setting the percentage in local preferences
                        int progress = jObj.getInt("progress");                //     "
                        cur.setPercentage(progress);

                    } else {
                        // Error in Submission
                        //button.setProgress(-1); //button is set at error colour - red
                        String errorMsg = jObj.getString("message"); //extracting the error
                        Log.d(TAG,"Message returned from server: " + errorMsg ); //logging the error message
                        Toast.makeText(getContext(),"An Error occured and logged, try Again", Toast.LENGTH_LONG).show(); //displaying an error to the user
                    }
                } catch (JSONException e) {
                   // button.setProgress(-1);
                    e.printStackTrace(); //logging error
                    //Toast.makeText(getContext(), "Internal error occured, try again later", Toast.LENGTH_LONG).show(); //displaying an error to the user
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //button.setProgress(-1);
                Log.e(TAG, "Volley Error: " + error.getMessage()); //error in android part logged
                Toast.makeText(getContext(),"Local error, try again", Toast.LENGTH_LONG).show(); //displaying the error to user
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                SQLiteHandler db= new SQLiteHandler(getContext());
                SessionManager cur = new SessionManager(getContext());
                String uid =db.getUserID();
                String uname=cur.getUsername();
                Map<String, String> params = new HashMap<String, String>();
                params.put("route", "4");
                params.put("userid", uid);
                params.put("username", uname);
                params.put("dummy", "blahBLAH");


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}




