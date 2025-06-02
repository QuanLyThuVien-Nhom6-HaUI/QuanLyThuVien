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
    private int currentPage;

    public NguoiDocAdapter(Context context, List<NguoiDoc> nguoiDocList, int currentPage) {
        this.context = context;
        this.nguoiDocList = nguoiDocList;
        this.currentPage = currentPage;
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
        TextView txtStt, txtTen, txtSDT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nguoidoc, parent, false);
            holder = new ViewHolder();
            holder.txtStt = convertView.findViewById(R.id.tvStt);
            holder.txtTen = convertView.findViewById(R.id.tvTen);
            holder.txtSDT = convertView.findViewById(R.id.tvSoDienThoai);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NguoiDoc nd = nguoiDocList.get(position);
        if (nd != null) {
            // Tính số thứ tự dựa trên currentPage và position
            int itemsPerPage = 10; // Số mục trên mỗi trang
            int startIndex = (currentPage - 1) * itemsPerPage + 1;
            int stt = startIndex + position;
            holder.txtStt.setText(String.valueOf(stt)); // Hiển thị số thứ tự
            holder.txtTen.setText(nd.getTenNguoiDoc());
            holder.txtSDT.setText(nd.getSoDienThoai());
        }

        return convertView;
    }

    public void setData(List<NguoiDoc> list) {
        this.nguoiDocList = list;
        notifyDataSetChanged();
    }
}