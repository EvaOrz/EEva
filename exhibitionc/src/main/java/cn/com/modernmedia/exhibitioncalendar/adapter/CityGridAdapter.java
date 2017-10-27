package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel.TagInfo;

/**
 * Created by Eva. on 2017/10/20.
 */
public class CityGridAdapter extends BaseAdapter {
    private List<TagListModel.TagInfo> tags = new ArrayList<TagInfo>();
    private LayoutInflater mInflater;
    private ViewHolder viewHolder = null;
    private RelativeLayout.LayoutParams layoutParams;
    private Context context;

    public CityGridAdapter(Context context, List<TagInfo> strList) {
        this.context = context;
        this.tags = strList;
        mInflater = LayoutInflater.from(context);
        int wid = (CommonApplication.width - 40) / 5 - 20;
        layoutParams = new RelativeLayout.LayoutParams(wid, wid);
        layoutParams.topMargin = 25;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Entry getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        convertView = mInflater.inflate(R.layout.item_city_select, null);
        final TagInfo t = tags.get(pos);
        /**
         * 控件初始化
         */
        viewHolder = new ViewHolder();
        viewHolder.back = (ImageView) convertView.findViewById(R.id.city_back);
        viewHolder.icon = (ImageView) convertView.findViewById(R.id.city_icon);
        viewHolder.titleEn = (TextView) convertView.findViewById(R.id.city_name_en);
        viewHolder.title = (TextView) convertView.findViewById(R.id.city_name);
        viewHolder.number = (TextView) convertView.findViewById(R.id.city_num);

        viewHolder.back.setLayoutParams(layoutParams);
        viewHolder.back.setImageResource(R.drawable.dot);
        CommonApplication.finalBitmap.display(viewHolder.icon, UrlMaker.HOST + t.getCityicon().getSourceV6Img());
        viewHolder.title.setText(t.getTagName());
        viewHolder.titleEn.setText(t.getTagNameEn());
        viewHolder.number.setText(t.getItemNum() + "");
        return convertView;
    }

    @SuppressLint("ViewHolder")
    // 通过ViewHolder显示项的内容
    static class ViewHolder {
        public ImageView icon;
        public ImageView back;
        public TextView title;
        public TextView titleEn;
        public TextView number;

    }
}