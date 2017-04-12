package cn.com.modernmedia.corelib.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.io.File;

import cn.com.modernmedia.corelib.R;


/**
 * 上传头像裁剪工具
 * Created by Eva. on 16/10/23.
 */

public class FetchPhotoManager implements View.OnClickListener {
    public static final int REQUEST_CAMERA = 100;
    public static final int REQUEST_GALLERY = 101;

    private Context mContext;
    private PopupWindow window;
    private String picturePath;

    public FetchPhotoManager(Context context, String picturePath) {
        mContext = context;
        this.picturePath = picturePath;
    }

    /**
     * 获取图片作为头像
     */
    public void doFecthPicture() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fetch_image_popup, null);
        window = new PopupWindow(view, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setAnimationStyle(R.style.fetch_image_popup_anim);
        window.update();
        window.setBackgroundDrawable(new BitmapDrawable());
        window.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        view.findViewById(R.id.fetch_image_from_camear).setOnClickListener(this);
        view.findViewById(R.id.fetch_image_from_gallery).setOnClickListener(this);
        view.findViewById(R.id.fetch_image_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fetch_image_from_camear) {
            window.dismiss();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(picturePath)));
            ((Activity) mContext).startActivityForResult(intent, REQUEST_CAMERA);
        } else if (id == R.id.fetch_image_from_gallery) {
            window.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            ((Activity) mContext).startActivityForResult(intent, REQUEST_GALLERY);
        } else if (id == R.id.fetch_image_cancel) {
            window.dismiss();
        }
    }

}
