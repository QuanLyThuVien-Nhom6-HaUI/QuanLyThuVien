package haui.nhom6.qlthuvien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.model.NguoiDoc;

public class NguoiDocAdapter extends BaseAdapter {
    private Context context;
    private List<NguoiDoc> nguoiDocList;

    public NguoiDocAdapter(Context context, List<NguoiDoc> nguoiDocList) {
        this.context = context;
        this.nguoiDocList = nguoiDocList;
    }

    @Override
    public int getCount() {
        return nguoiDocList.size();
    }

    @Override
    public Object getItem(int position) {
        return nguoiDocList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView txtTen, txtSDT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nguoidoc, parent, false);
            holder = new ViewHolder();
            holder.txtTen = convertView.findViewById(R.id.tvTen);
            holder.txtSDT = convertView.findViewById(R.id.tvSoDienThoai);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NguoiDoc nd = nguoiDocList.get(position);
        holder.txtTen.setText(nd.getTenNguoiDoc());
        holder.txtSDT.setText(nd.getSoDienThoai());

        return convertView;
    }
}