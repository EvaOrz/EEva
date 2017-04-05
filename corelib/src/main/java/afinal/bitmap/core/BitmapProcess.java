package afinal.bitmap.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import afinal.bitmap.download.Downloader;
import modernmedia.com.cn.corelib.util.ImgFileManager;


public class BitmapProcess {
    private Downloader mDownloader;
    @SuppressWarnings("unused")
    private BitmapCache mCache;

    private static final int BYTESBUFFE_POOL_SIZE = 4;
    private static final int BYTESBUFFER_SIZE = 200 * 1024;
    @SuppressWarnings("unused")
    private static final BytesBufferPool sMicroThumbBufferPool = new BytesBufferPool(BYTESBUFFE_POOL_SIZE, BYTESBUFFER_SIZE);

    public BitmapProcess(Downloader downloader, BitmapCache cache) {
        this.mDownloader = downloader;
        this.mCache = cache;
    }

    public Bitmap getBitmap(String url, BitmapDisplayConfig config) {

        Bitmap bitmap = getFromDisk(url, config);

        if (bitmap == null) {
            byte[] data = mDownloader.download(url);
            if (data != null && data.length > 0) {
                if (config != null)
                    bitmap = BitmapDecoder.decodeSampledBitmapFromByteArray(data, 0, data.length, config.getBitmapWidth(), config.getBitmapHeight());
                else return BitmapFactory.decodeByteArray(data, 0, data.length);

                ImgFileManager.saveImage(url, data);
            }
        }

        return bitmap;
    }

    /**
     * 获取动图byte []
     *
     * @param url
     * @return
     */
    public byte[] getMovie(String url) {
        byte[] data = ImgFileManager.getImage(url);
        if (data == null) {
            data = mDownloader.download(url);
            if (data != null && data.length > 0) ImgFileManager.saveImage(url, data);
        }
        return data;
    }

    public Bitmap getFromDisk(String key, BitmapDisplayConfig config) {
        // BytesBuffer buffer = sMicroThumbBufferPool.get();
        Bitmap b = null;
        // try {
        // boolean found = mCache.getImageData(key, buffer);
        // if (found && buffer.length - buffer.offset > 0) {
        byte[] data = ImgFileManager.getImage(key);
        if (data == null) return null;
        if (config != null) {
            b = BitmapDecoder.decodeSampledBitmapFromByteArray(data, 0, data.length, config.getBitmapWidth(), config.getBitmapHeight());
        } else {
            b = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        // }
        // } finally {
        // sMicroThumbBufferPool.recycle(buffer);
        // }
        return b;
    }

}
