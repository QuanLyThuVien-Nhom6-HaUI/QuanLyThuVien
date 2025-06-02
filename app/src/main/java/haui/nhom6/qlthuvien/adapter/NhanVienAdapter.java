package haui.nhom6.qlthuvien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.model.NhanVien;

public class NhanVienAdapter extends BaseAdapter {
    private Context context;
    private List<NhanVien> nhanVienList;
    private int currentPage;  // trang hiện tại (bắt đầu từ 1)
    private int pageSize;     // số nhân viên trên mỗi trang

    public NhanVienAdapter(Context context, List<NhanVien> nhanVienList, int currentPage, int pageSize) {
        this.context = context;
        this.nhanVienList = nhanVienList;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
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

    private static class ViewHolder {
        TextView tvSoThuTu;
        TextView tvTen;
        TextView tvSDT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nhanvien, parent, false);
            holder = new ViewHolder();
            holder.tvSoThuTu = convertView.findViewById(R.id.tvSoThuTu);
            holder.tvTen = convertView.findViewById(R.id.tvTen);
            holder.tvSDT = convertView.findViewById(R.id.tvSoDienThoai);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NhanVien nv = nhanVienList.get(position);

        // Tính số thứ tự theo phân trang:
        // (Trang hiện tại - 1) * pageSize + vị trí trong trang + 1
        int soThuTu = (currentPage - 1) * pageSize + position + 1;
        holder.tvSoThuTu.setText(String.valueOf(soThuTu));

        holder.tvTen.setText(nv.getTenNhanVien());
        holder.tvSDT.setText(nv.getSoDienThoai());

        return convertView;
    }

    // Setter để cập nhật trang hiện tại nếu cần
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
