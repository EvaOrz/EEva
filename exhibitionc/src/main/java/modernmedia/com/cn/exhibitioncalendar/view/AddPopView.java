package modernmedia.com.cn.exhibitioncalendar.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import modernmedia.com.cn.exhibitioncalendar.R;

/**
 * Created by Eva. on 17/4/11.
 */

public class AddPopView {


    private Context mContext;
    private PopupWindow window;

    public AddPopView(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
    
        View view = LayoutInflater.from(mContext).inflate(R.layout.fetch_image_popup, null);
        window = new

                PopupWindow(view, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setAnimationStyle(R.style.fetch_image_popup_anim);
        window.update();
        window.setBackgroundDrawable(new

                BitmapDrawable());
        window.showAtLocation(view, Gravity.BOTTOM, 0, 0);

    }

}
