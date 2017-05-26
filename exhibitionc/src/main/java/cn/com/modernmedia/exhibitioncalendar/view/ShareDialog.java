package cn.com.modernmedia.exhibitioncalendar.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.util.ShareTools;


/**
 * Created by Eva. on 17/4/17.
 */

public class ShareDialog extends PopupWindow

{
    private Context mContext;
    private View conentView;
    private PopupWindow window;
    private ShareTools shareTools;


    private void init() {
        shareTools = ShareTools.getInstance(mContext);
        conentView = LayoutInflater.from(mContext).inflate(R.layout.view_share_dialog, null);
        conentView.findViewById(R.id.share_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        window = new PopupWindow(conentView, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setAnimationStyle(R.style.fetch_image_popup_anim);
        window.update();
        window.setBackgroundDrawable(new BitmapDrawable());
        window.showAtLocation(conentView, Gravity.BOTTOM, 0, 0);

    }

    /**
     * 下载分享图片
     *
     * @param sharePic
     */
    private void prepareShare(String sharePic) {
        String url = "";
        if (!TextUtils.isEmpty(sharePic)) shareTools.prepareShareAfterFetchBitmap(url);
    }


    /**
     * 分享文章
     *
     * @param context
     * @param calendarModel
     */
    public ShareDialog(final Context context, final CalendarModel calendarModel) {
        mContext = context;
        init();
        conentView.findViewById(R.id.share_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("邮件分享", "邮件分享");
                shareTools.shareByMail(calendarModel.getTitle(), calendarModel.getContent(), calendarModel.getWeburl());

            }
        });
        // 微信分享
        conentView.findViewById(R.id.share_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTools.shareImageAndTextToWeixin(calendarModel.getTitle(), calendarModel.getContent(), calendarModel.getWeburl(), MyApplication.finalBitmap.getBitmapFromCache(calendarModel.getBackgroundImg()), false);

            }
        });
        conentView.findViewById(R.id.share_pengyouquan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTools.shareImageAndTextToWeixin(calendarModel.getTitle(), calendarModel.getContent(), calendarModel.getWeburl(), MyApplication.finalBitmap.getBitmapFromCache(calendarModel.getBackgroundImg()), true);

            }
        });
        // 微博分享
        conentView.findViewById(R.id.share_sina).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cc = shareTools.getWeiBoContent(calendarModel.getTitle(), calendarModel.getContent(), calendarModel.getWeburl());
                shareTools.shareWithSina(cc, MyApplication.finalBitmap.getBitmapFromCache(calendarModel
                        .getBackgroundImg()));
            }
        });
        //qq分享
        //        conentView.findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //
        //            }
        //        });

        if (!TextUtils.isEmpty(calendarModel.getBackgroundImg()))
            prepareShare(calendarModel.getBackgroundImg());
    }


    /**
     * 分享行程
     *
     * @param context
     */
    public ShareDialog(final Context context, final String title, final String content, final String avatar, final String webUrl) {
        mContext = context;
        init();
        conentView.findViewById(R.id.share_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("邮件分享", "邮件分享");
                shareTools.shareByMail(title, content, webUrl);

            }
        });
        // 微信分享
        conentView.findViewById(R.id.share_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTools.shareImageAndTextToWeixin(title, content, webUrl, MyApplication.finalBitmap.getBitmapFromCache(avatar), false);

            }
        });
        conentView.findViewById(R.id.share_pengyouquan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTools.shareImageAndTextToWeixin(title, content, webUrl, MyApplication.finalBitmap.getBitmapFromCache(avatar), true);

            }
        });
        // 微博分享
        conentView.findViewById(R.id.share_sina).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTools.shareWithSina(shareTools.getWeiBoContent(title, content, webUrl), MyApplication.finalBitmap.getBitmapFromCache(avatar));
            }
        });
        //qq分享
        //        conentView.findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //
        //            }
        //        });

        if (!TextUtils.isEmpty(avatar)) prepareShare(avatar);
    }


    public void showViewFromBo(View v) {
        if (this.isShowing()) {
            dismiss();
        } else {

            this.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 显示 隐藏
     */
    public void showView(View v) {
        if (this.isShowing()) {
            dismiss();
        } else {

            this.showAsDropDown(v);
        }
    }
}