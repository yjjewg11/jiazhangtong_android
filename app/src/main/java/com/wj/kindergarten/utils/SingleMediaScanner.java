<<<<<<< HEAD
package com.wj.kindergarten.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * SingleMediaScanner
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/24 17:41
 */
public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {
    private MediaScannerConnection mMs;
    private File mFile;
    private Scan scan;

    public SingleMediaScanner(Context context, File f, Scan scan) {
        mFile = f;
        this.scan = scan;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        Log.d("CropHelper", "Scan connected");
        mMs.scanFile(mFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        Log.d("CropHelper", "Scan completed");
        mMs.disconnect();
        if (scan != null) {
            scan.scanCompleted();
        }


    }

    public interface Scan {
        public void scanCompleted();
    }
}
=======
package com.wj.kindergarten.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * SingleMediaScanner
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/24 17:41
 */
public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {
    private MediaScannerConnection mMs;
    private File mFile;
    private Scan scan;

    public SingleMediaScanner(Context context, File f, Scan scan) {
        mFile = f;
        this.scan = scan;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        Log.d("CropHelper", "Scan connected");
        mMs.scanFile(mFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        Log.d("CropHelper", "Scan completed");
        mMs.disconnect();
        if (scan != null) {
            scan.scanCompleted();
        }


    }

    public interface Scan {
        public void scanCompleted();
    }
}
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
