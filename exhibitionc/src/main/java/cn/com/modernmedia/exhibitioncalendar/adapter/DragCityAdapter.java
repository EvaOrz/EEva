package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.CityPickActivity;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel.TagInfo;


/**
 * Created by xiaolin on 2015/1/24.
 */
public class DragCityAdapter extends BaseAdapter {
    private Context context;
    private List<TagInfo> tags = new ArrayList<TagInfo>();
    private int hidePosition = AdapterView.INVALID_POSITION;
    private LayoutInflater mInflater;
    private ViewHolder viewHolder = null;
    private RelativeLayout.LayoutParams layoutParams;

    public DragCityAdapter(Context context, List<TagInfo> strList) {
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
        viewHolder.delete = (ImageView) convertView.findViewById(R.id.city_delete);
        viewHolder.number = (TextView) convertView.findViewById(R.id.city_num);
        convertView.setTag(t);

        viewHolder.back.setLayoutParams(layoutParams);
        viewHolder.back.setImageResource(R.drawable.dot);
        CommonApplication.finalBitmap.display(viewHolder.icon, UrlMaker.HOST + t.getCityicon().getSourceV6Img());
        viewHolder.title.setText(t.getTagName());
        viewHolder.titleEn.setText(t.getTagNameEn());
        viewHolder.number.setText(t.getMuseumNum()+"");

        // hide时隐藏Text
        if (pos != hidePosition) {

            if (t.isCheck())// 排序状态
            {
                viewHolder.delete.setVisibility(View.VISIBLE);
            } else {
                // 无法取消状态
                viewHolder.delete.setVisibility(View.GONE);
            }
            viewHolder.delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((CityPickActivity) context).deleteBook(pos);
                }
            });
        }

        return convertView;
    }

    public void hideView(int pos) {
        hidePosition = pos;
        notifyDataSetChanged();
    }

    public void showHideView() {
        hidePosition = AdapterView.INVALID_POSITION;
        notifyDataSetChanged();
    }

    /**
     * 判断可拖动的区域
     *
     * @param draggedPos
     * @param destPos
     */
    // 更新拖动时的gridView
    public boolean swapView(int draggedPos, int destPos) {

        // 从前向后拖动，其他item依次前移
        if (draggedPos < destPos) {
            tags.add(destPos + 1, (TagInfo) getItem(draggedPos));
            tags.remove(draggedPos);
        }
        // 从后向前拖动，其他item依次后移
        else if (draggedPos > destPos) {
            tags.add(destPos, (TagInfo) getItem(draggedPos));
            tags.remove(draggedPos + 1);
        }
        hidePosition = destPos;
        notifyDataSetChanged();
        return true;
    }

    // 通过ViewHolder显示项的内容
    static class ViewHolder {
        public ImageView icon;
        public ImageView back;
        public ImageView delete;
        public TextView title;
        public TextView titleEn;
        public TextView number;
    }

    public List<TagInfo> getTagsList() {
        return tags;
    }
}
