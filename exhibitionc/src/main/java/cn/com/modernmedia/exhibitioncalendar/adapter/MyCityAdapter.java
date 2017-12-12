package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel.TagInfo;

/**
 * Created by Eva. on 2017/12/7.
 */

public class MyCityAdapter extends BaseAdapter {
    private Context context;
    private List<TagInfo> tags = new ArrayList<TagInfo>();
    private LayoutInflater mInflater;
    private ViewHolder viewHolder = null;

    public MyCityAdapter(Context context, List<TagInfo> strList) {
        this.context = context;
        this.tags = strList;
        mInflater = LayoutInflater.from(context);
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
        convertView = mInflater.inflate(R.layout.item_my_city, null);
        final TagInfo t = tags.get(pos);
        /**
         * 控件初始化
         */
        viewHolder = new ViewHolder();
        viewHolder.icon = (ImageView) convertView.findViewById(R.id.my_city_icon);
        viewHolder.name = (TextView) convertView.findViewById(R.id.my_city_name);
        viewHolder.number = (TextView) convertView.findViewById(R.id.my_city_num);
        convertView.setTag(t);

        CommonApplication.finalBitmap.display(viewHolder.icon, UrlMaker.HOST + t.getCityicon().getSourceV6Img());
        viewHolder.name.setText(t.getTagName() +" "+t.getTagNameEn());
        viewHolder.number.setText(t.getMuseumNum() + "");


        return convertView;
    }



    // 通过ViewHolder显示项的内容
    static class ViewHolder {
        public ImageView icon;
        public TextView name;
        public TextView number;
    }

    public List<TagInfo> getTagsList() {
        return tags;
    }
}