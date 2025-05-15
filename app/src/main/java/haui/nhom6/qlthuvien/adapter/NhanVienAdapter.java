package haui.nhom6.qlthuvien.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.List;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.model.NhanVien;

public class NhanVienAdapter extends BaseAdapter {
    private Context context;
    private List<NhanVien> nhanVienList;

    public NhanVienAdapter(Context context, List<NhanVien> nhanVienList) {
        this.context = context;
        this.nhanVienList = nhanVienList;
    }

    @Override
    public int getCount() {
        return nhanVienList.size();
    }

    @Override
    public Object getItem(int position) {
        return nhanVienList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nhanvien, parent, false);
            holder = new ViewHolder();
            holder.txtTen = convertView.findViewById(R.id.tvTen);
            holder.txtSDT = convertView.findViewById(R.id.tvSoDienThoai);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NhanVien nv = nhanVienList.get(position);
        holder.txtTen.setText(nv.getTenNhanVien());
        holder.txtSDT.setText(nv.getSoDienThoai());

        return convertView;
    }
}
