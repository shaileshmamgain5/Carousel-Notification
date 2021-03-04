package in.mamga.carousalnotification;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Downloads carousal images and saves it into a file
 * @author Vadim Zuev
 * @version 1.1
 */
public class BasicImageDownloader {

    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private ArrayList<CarousalItem> carousalItems;
    private OnDownloadsCompletedListener onDownloadsCompletedListener;
    int numberOfImages;
    static int currentDownloadTaskIndex = 0;
    CarousalItem currentItem;

    public BasicImageDownloader(Context context, ArrayList<CarousalItem> carousalItems, int numberOfImages, @NonNull OnDownloadsCompletedListener onDownloadsCompletedListener) {
        this.carousalItems = carousalItems;
        this.context = context;
        this.onDownloadsCompletedListener = onDownloadsCompletedListener;
        this.numberOfImages = numberOfImages;
    }

    private OnImageLoaderListener mImageLoaderListener = new OnImageLoaderListener() {
        @Override
        public void onError(ImageError error) {
            updateDownLoad(null);

        }

        @Override
        public void onComplete(String resultPath) {
            updateDownLoad(resultPath);

        }
    };

    private void updateDownLoad(String filePath) {

        for (int i = (currentDownloadTaskIndex + 1); i < carousalItems.size(); i++) {
            if (!TextUtils.isEmpty(carousalItems.get(i).getPhoto_url())) {
                currentDownloadTaskIndex = i;
                currentItem = carousalItems.get(i);
                downloadImage(currentItem.getPhoto_url());
                break;
            }
        }
        --numberOfImages;
        if (numberOfImages < 1 || currentDownloadTaskIndex > carousalItems.size() - 1) {
            onDownloadsCompletedListener.onComplete();
        }
    }

    public void startAllDownloads() {
        if (carousalItems != null && carousalItems.size() > 0) {
            for (int i = 0; i < carousalItems.size(); i++) {
                if (!TextUtils.isEmpty(carousalItems.get(i).getPhoto_url())) {
                    currentDownloadTaskIndex = i;
                    currentItem = carousalItems.get(i);
                    downloadImage(currentItem.getPhoto_url());
                    break;
                }
            }
        }
    }

    /**
     * Interface definition for callbacks to be invoked
     * when the image download status changes.
     */
    public interface OnImageLoaderListener {
        /**
         * Invoked if an error has occurred and thus
         * the download did not complete
         *
         * @param error the occurred error
         */
        void onError(ImageError error);

        /**
         * Invoked after the image has been successfully downloaded
         *
         * @param resultPath the downloaded image
         */
        void onComplete(String resultPath);
    }

    /**
     * Interface definition for callbacks to be invoked
     * when the image download status changes.
     */
    public interface OnDownloadsCompletedListener {

        /**
         * invoked after all files are downloaded
         */
        void onComplete();
    }

    /**
     * Downloads the image from the given URL using an {@link AsyncTask}. If a download
     * for the given URL is already in progress this method returns immediately.
     *
     * @param imageUrl the URL to get the image from
     */
    public void downloadImage(@NonNull final String imageUrl) {

        new AsyncTask<Void, Integer, String>() {

            private ImageError error;
            private long currentTimeInMillis;


            @Override
            protected void onCancelled() {
                mImageLoaderListener.onError(error);
            }

            @Override
            protected String doInBackground(Void... params) {
                currentTimeInMillis = System.currentTimeMillis();
                Bitmap bitmap = null;
                String imagePath = null;
                HttpURLConnection connection = null;
                InputStream is = null;
                ByteArrayOutputStream out = null;
                try {
                    connection = (HttpURLConnection) new URL(imageUrl).openConnection();
                    is = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    if (bitmap != null) {
                        //scaling bitmap to ensure minimum memory usage
                        int sampleSize = CarousalUtilities.carousalCalculateInSampleSize(bitmap.getWidth(), bitmap.getHeight(), 250, 250);
                        Bitmap bit = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/sampleSize, bitmap.getHeight()/sampleSize, false);
                        imagePath = CarousalUtilities.carousalSaveBitmapToInternalStorage(context, bit, CarousalConstants.CAROUSAL_IMAGE_BEGENNING + currentTimeInMillis);
                    }

                } catch (Throwable e) {
                    if (!this.isCancelled()) {
                        error = new ImageError(e).setErrorCode(ImageError.ERROR_GENERAL_EXCEPTION);
                        this.cancel(true);
                    }
                } finally {
                    try {
                        if (connection != null)
                            connection.disconnect();
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                        if (is != null)
                            is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return imagePath;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result == null) {
                    Log.e(TAG, "factory returned a null result");
                    mImageLoaderListener.onError(new ImageError("downloaded file could not be decoded as bitmap")
                            .setErrorCode(ImageError.ERROR_DECODE_FAILED));
                } else {
                    Log.d(TAG, "download complete");
                    if (currentItem != null)
                        currentItem.setImage_file_location(result);
                        currentItem.setImage_file_name(CarousalConstants.CAROUSAL_IMAGE_BEGENNING + currentTimeInMillis);
                    mImageLoaderListener.onComplete(result);
                }
                System.gc();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /**
     * Represents an error that has occurred while
     * downloading image or writing it to disk. Since
     * this class extends {@code Throwable}, you may get the
     * stack trace from an {@code ImageError} object
     */
    public static final class ImageError extends Throwable {

        private int errorCode;
        /**
         * An exception was thrown during an operation.
         * Check the error message for details.
         */
        public static final int ERROR_GENERAL_EXCEPTION = -1;
        /**
         * The URL does not point to a valid file
         */
        public static final int ERROR_INVALID_FILE = 0;
        /**
         * The downloaded file could not be decoded as bitmap
         */
        public static final int ERROR_DECODE_FAILED = 1;


        public ImageError(@NonNull String message) {
            super(message);
        }

        public ImageError(@NonNull Throwable error) {
            super(error.getMessage(), error.getCause());
            this.setStackTrace(error.getStackTrace());
        }

        /**
         * @param code the code for the occurred error
         * @return the same ImageError object
         */
        public ImageError setErrorCode(int code) {
            this.errorCode = code;
            return this;
        }

        /**
         * @return the error code that was previously set
         * by {@link #setErrorCode(int)}
         */
        public int getErrorCode() {
            return errorCode;
        }
    }
}
