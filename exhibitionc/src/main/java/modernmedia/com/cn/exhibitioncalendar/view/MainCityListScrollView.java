package modernmedia.com.cn.exhibitioncalendar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import modernmedia.com.cn.corelib.BaseActivity;

/**
 * top_menu view
 *
 * @author lusiyuan
 */
public class MainCityListScrollView extends RelativeLayout {

    private Context mContext;
    private HorizontalScrollView scrollView;
    private LinearLayout layout;
    private List<View> checkSelectView = new ArrayList<View>();// 记录栏目列表
    /**
     * 由于可能是独立栏目，并且绑定在列表上，导致每次切换的时候都还原，所以设置成静态变量
     */
    public static int selectPosition = -1;


    public MainCityListScrollView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public void setCorruntPosition(int p) {
        selectPosition = p;
    }

    public MainCityListScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        scrollView = new HorizontalScrollView(mContext);
        addView(scrollView);


    }


    /**
     * 设置子栏目选中状态
     *
     * @param currTagName
     */
    @SuppressLint("NewApi")
    // int[] location = new int[2];
    // child.getLocationOnScreen(location);
    // int x = location[0];
    public void setSelectedItemForChild(String currTagName) {
        if (layout == null || layout.getChildCount() == 0) return;
        int screenHalf = 0;// 屏幕宽度的一半
        if (mContext instanceof BaseActivity)
            screenHalf = ((BaseActivity) mContext).getWindowManager().getDefaultDisplay().getWidth() / 2 - 44 * 2 - 20;
        for (int i = 0; i < layout.getChildCount(); i++) {

            View child = layout.getChildAt(i);
            //            if (child.getTag() instanceof TagInfo) {
            //
            //                // 选中状态
            //                if (((TagInfo) child.getTag()).getTagName().equals(currTagName)) {
            //                    selectPosition = i;
            //                    // 商周设置为 栏目颜色
            //                    if (DataHelper.columnColorMap.containsKey(currTagName) && ConstData.getAppId() == 1) {
            //                        int color = DataHelper.columnColorMap.get(currTagName);
            //                        TextView t = (TextView) ((LinearLayout) child).getChildAt(0);
            //                        t.setBackgroundColor(color);
            //                        t.setTextColor(Color.WHITE);
            //                    } else V.setViewBack(child, "#323232");
            //                    int scrollX = scrollView.getScrollX();
            //                    int left = child.getLeft();
            //                    int right = child.getRight();
            //                    int leftScreen = left - scrollX + (right - left) / 2;
            //                    scrollView.smoothScrollBy((leftScreen - screenHalf), 0);
            //                } else {
            //                    if (ConstData.getAppId() == 1) {
            //                        TextView t = (TextView) ((LinearLayout) child).getChildAt(0);
            //                        V.setViewBack(t, "#fff0f0f0");
            //                        t.setTextColor(Color.BLACK);
            //                    } else V.setViewBack(child, "#191919");
            //
            //                }
            //            }
        }
    }
}

