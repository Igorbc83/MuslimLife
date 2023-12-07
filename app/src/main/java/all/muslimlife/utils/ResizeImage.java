package all.muslimlife.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Base64OutputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ResizeImage {

    private static int SHORTER_SIDE_TARGET = 384;
    private static int MAX_IMAGE_SIZE = SHORTER_SIDE_TARGET * SHORTER_SIDE_TARGET;
    private static int IMAGE_QUALITY = 70;

    public static Bitmap compressFile(File file) {
        Bitmap bmpPic = BitmapFactory.decodeFile(file.getAbsolutePath());

        Bitmap resizedBitmap = calculateImageSize(bmpPic);

        int compressQuality = IMAGE_QUALITY;
        int streamLength = MAX_IMAGE_SIZE;
        while (streamLength >= MAX_IMAGE_SIZE) {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            compressQuality -= 5;
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
        }

        return resizedBitmap;
    }

    public static Bitmap compressBitmap(Bitmap bmpPic) {
        Bitmap resizedBitmap = calculateImageSize(bmpPic);

        int compressQuality = IMAGE_QUALITY;
        int streamLength = MAX_IMAGE_SIZE;
        while (streamLength >= MAX_IMAGE_SIZE) {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            compressQuality -= 5;
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
        }

        return resizedBitmap;
    }

    public static Bitmap calculateImageSize(Bitmap targetBitmap) {

        int imageWidth = targetBitmap.getWidth();
        int imageHeight = targetBitmap.getHeight();

        float ratio = (float) imageWidth / imageHeight;

        int targetWidth;
        int targetHeight;

        if (imageWidth > imageHeight) {
            targetHeight = SHORTER_SIDE_TARGET;
            targetWidth = Math.round(SHORTER_SIDE_TARGET * ratio);
        }
        else {
            targetWidth = SHORTER_SIDE_TARGET;
            targetHeight = Math.round(SHORTER_SIDE_TARGET / ratio);
        }

        return Bitmap.createScaledBitmap(targetBitmap, targetWidth, targetHeight, false);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile= "", lastVal;
        try {
            inputStream = new FileInputStream(f);

            byte[] buffer = new byte[224000];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile =  output.toString();
        }
        catch (FileNotFoundException e1 ) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }
}

