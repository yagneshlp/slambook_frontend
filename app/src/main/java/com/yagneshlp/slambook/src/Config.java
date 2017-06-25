package com.yagneshlp.slambook.src;

import android.util.Base64;

/**
 * Created by Yagnesh L P on 10-06-2017.
 */

public class Config {
    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = "http://@slambook.yagneshlp.com/app/slambook_api/upload.php";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Slambook Photo";

    static final String credentials = "admin:randommonkeY9";
    public static final String auth = "Basic "
            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
}