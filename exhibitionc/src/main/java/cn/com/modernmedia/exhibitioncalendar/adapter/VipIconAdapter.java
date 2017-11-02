package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.model.ProductListModel;
import cn.com.modernmedia.exhibitioncalendar.model.ProductListModel.ProductModel;

/**
 * Created by Eva. on 2017/10/30.
 */

public class VipIconAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<ProductListModel> datas = new ArrayList<ProductListModel>();

    public VipIconAdapter(Context context, List<ProductListModel> strList) {
        this.context = context;
        this.datas = strList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public ProductListModel getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        convertView = mInflater.inflate(R.layout.item_vip_icon, null);

        ImageView icon1 = (ImageView) convertView.findViewById(R.id.vip_icon1);
        ImageView icon2 = (ImageView) convertView.findViewById(R.id.vip_icon2);
        ImageView icon3 = (ImageView) convertView.findViewById(R.id.vip_icon3);
        TextView desc = (TextView) convertView.findViewById(R.id.vip_icon_desc);

        final ProductListModel t = datas.get(pos);
        if (t != null && ParseUtil.listNotNull(t.getList())) {
            if (t.getList().size() > 0) initImage(t.getList().get(0), icon1, desc);
            if (t.getList().size() > 1) initImage(t.getList().get(1), icon2, desc);
            if (t.getList().size() > 2) initImage(t.getList().get(2), icon3, desc);
        }

        return convertView;
    }

    private void initImage(final ProductModel t, final ImageView i, final TextView desc) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((CommonApplication.width - 80) / 3, ((CommonApplication.width - 80) / 3) * 46 / 55);
        params.setMargins(10, 10, 10, 10);
        i.setLayoutParams(params);

        if (t == null || TextUtils.isEmpty(t.getPid())) i.setVisibility(View.GONE);
        i.setTag(t.getGoodDesc());
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc.isShown()) {
                    desc.setVisibility(View.GONE);
                } else {
                    desc.setVisibility(View.VISIBLE);
                }
                desc.setText((String) v.getTag());
            }
        });
        if (t.getBright() == 0) {
            Tools.setImage(i, t.getPid() + "_default");
        } else if (t.getBright() == 1) {
            Tools.setImage(i, t.getPid());
        }
    }


}
