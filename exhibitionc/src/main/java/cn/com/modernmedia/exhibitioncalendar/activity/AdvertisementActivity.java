package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.model.AdvListModel;
import cn.com.modernmedia.exhibitioncalendar.model.AdvListModel.AdvItem;

/**
 * 入版广告显示页面
 * Created by Eva. on 17/3/28.
 */

public class AdvertisementActivity extends BaseActivity {

    public static final int IBB_DURATION = 1000;
    public static final int ILOHAS_DURATION = 2000;// 乐活默认时间(其实是3s，动画完了还有1s延迟)
    public static final int IWEEKLY_DURATION = 2000;
    public static final List<String> EFFECT_LIST = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            add(AdvListModel.IBB);
            add(AdvListModel.ILOHAS);
            add(AdvListModel.IWEEKLY);
        }
    };
    public static String ADVACTIVITY_PIC_LIST = "advactivity_pic_list";
    public static String ADVACTIVITY_ADV_ITEM = "advactivity_pic_list";
    private boolean canGoMain = true;// 防止重复进入main
    private long lastTime;
    private ImageView imageView;
    private ViewFlipper flipper;
    private ArrayList<String> picList;
    private AdvItem advItem;
    private String effect;
    private AlphaAnimation alphaOut;
    private AlphaAnimation alphaIn;
    private int current;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                gotoMainActivity();
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        if (fetchDataFromIntent() && compareTime()) init();
        else gotoMainActivity();
        if (compareTime()) DataHelper.setAdvTime(this, System.currentTimeMillis());
    }

    /**
     * 入版广告(5分钟内不重复提示)
     *
     * @return true提示；false不提示
     */
    private boolean compareTime() {
        lastTime = DataHelper.getAdvTime(this);
        if (lastTime == 0) {
            return true;
        }
        long minute = (System.currentTimeMillis() - lastTime) / (1000 * 60);
        return minute > 5;
    }

    private boolean fetchDataFromIntent() {
        if (getIntent() == null || getIntent().getExtras() == null) {
            return false;
        }
        picList = getIntent().getExtras().getStringArrayList(ADVACTIVITY_PIC_LIST);
        if (!ParseUtil.listNotNull(picList)) return false;
        Object object = getIntent().getExtras().get(ADVACTIVITY_ADV_ITEM);
        if (object instanceof AdvItem) {
            advItem = (AdvItem) object;
            return true;
        }
        return false;
    }

    private void init() {
        imageView = (ImageView) findViewById(R.id.adv_image);
        flipper = (ViewFlipper) findViewById(R.id.adv_flipper);
        // 跳出
        findViewById(R.id.adv_imgo).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gotoMainActivity();
            }
        });


        effect = advItem.getEffects();
        if (TextUtils.isEmpty(effect) || !EFFECT_LIST.contains(effect)) effect = EFFECT_LIST.get(0);
        alphaOut = new AlphaAnimation(0.5f, 1.0f);
        alphaOut.setFillAfter(true);
        alphaOut.setDuration(getDuration(EFFECT_LIST.get(2)));
        alphaOut.setInterpolator(new LinearInterpolator());
        alphaIn = new AlphaAnimation(.2f, 0.0f);
        alphaIn.setDuration(getDuration(EFFECT_LIST.get(2)));
        alphaIn.setInterpolator(new LinearInterpolator());
        alphaOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (current < picList.size() - 1) {
                    doFlipper();
                    current++;
                } else {
                    gotoMainActivity();
                }
            }
        });
        doEffect();
    }

    private void doEffect() {
        if (effect.equals(EFFECT_LIST.get(0))) {
            imageView.setImageBitmap(CommonApplication.finalBitmap.getBitmapFromDiskCache(picList.get(0)));
            doHoldAnim(effect);
        } else if (effect.equals(EFFECT_LIST.get(1))) {
            imageView.setImageBitmap(CommonApplication.finalBitmap.getBitmapFromDiskCache(picList.get(0)));
            ScaleAnimation scaleAnimation = new ScaleAnimation(1.2f, 1f, 1.2f, 1f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
            scaleAnimation.setDuration(getDuration(effect));
            scaleAnimation.setInterpolator(new LinearInterpolator());
            scaleAnimation.setFillAfter(true);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    handler.sendEmptyMessageDelayed(100, 1000);
                }
            });
            imageView.startAnimation(scaleAnimation);
        } else if (effect.equals(EFFECT_LIST.get(2))) {
            imageView.setVisibility(View.GONE);
            flipper.setVisibility(View.VISIBLE);
            for (String url : picList) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setImageBitmap(CommonApplication.finalBitmap.getBitmapFromDiskCache(url));
                flipper.addView(imageView);
            }
            if (flipper.getChildCount() > 0) flipper.getChildAt(0).startAnimation(alphaOut);
            else doHoldAnim(effect);
        }
    }

    private void doFlipper() {
        flipper.setInAnimation(alphaOut);
        flipper.setOutAnimation(alphaIn);
        flipper.showNext();
    }

    /**
     * 执行hold动画
     */
    private void doHoldAnim(final String effect) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                gotoMainActivity();
            }
        }, getDuration(effect));
    }

    ;

    /**
     * 进入首页
     */
    private void gotoMainActivity() {
        if (!canGoMain) return;
        canGoMain = false;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.alpha_out_1s, R.anim.alpha_in_1s);
    }

    /**
     * 获取动画持续时间
     *
     * @param effect
     * @return
     */
    private int getDuration(String effect) {
        int close = advItem.getAutoClose() * 1000;
        if (TextUtils.equals(effect, EFFECT_LIST.get(0))) {
            return close == 0 ? IBB_DURATION : close;
        } else if (TextUtils.equals(effect, EFFECT_LIST.get(1))) {
            return close == 0 ? ILOHAS_DURATION : close;
        }
        return close == 0 ? IWEEKLY_DURATION : close;
    }
}
