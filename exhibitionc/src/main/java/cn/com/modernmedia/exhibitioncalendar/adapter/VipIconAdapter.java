package cn.com.modernmedia.exhibitioncalendar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.model.ProductListModel.ProductModel;

/**
 * Created by Eva. on 2017/10/30.
 */

public class VipIconAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<ProductModel> datas = new ArrayList<ProductModel>();

    public VipIconAdapter(Context context, List<ProductModel> strList) {
        this.context = context;
        this.datas = strList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public ProductModel getItem(int position) {
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
        ImageView icon = (ImageView) convertView.findViewById(R.id.vip_icon);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (CommonApplication.width / 3 - 40) * 46 / 55);
        icon.setLayoutParams(params);
        final ProductModel t = datas.get(pos);
        if (t.getBright() == 0) {
            Tools.setImage(icon, t.getPid()+"_default");
        } else if (t.getBright() == 1) {
            Tools.setImage(icon, t.getPid());
        }


        return convertView;
    }


}
