package haui.nhom6.qlthuvien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.model.Sach;
import java.util.List;

public class SachAdapter extends ArrayAdapter<Sach> {

    private int currentPage;

    public SachAdapter(@NonNull Context context, @NonNull List<Sach> objects, int currentPage) {
        super(context, 0, objects);
        this.currentPage = currentPage;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sach, parent, false);
        }

        Sach sach = getItem(position);

        TextView tvStt = convertView.findViewById(R.id.tvStt);
        TextView tvMaSach = convertView.findViewById(R.id.tvMaSach);
        TextView tvTenSach = convertView.findViewById(R.id.tvTenSach);
        TextView tvTacGia = convertView.findViewById(R.id.tvTacGia);
        TextView tvTheLoai = convertView.findViewById(R.id.tvTheLoai);

        if (sach != null) {
            // Tính số thứ tự dựa trên currentPage và position
            int itemsPerPage = 10; // Số mục trên mỗi trang
            int startIndex = (currentPage - 1) * itemsPerPage + 1;
            int stt = startIndex + position;
            tvStt.setText(String.valueOf(stt)); // Hiển thị số thứ tự
            tvMaSach.setText(sach.getMaSach());
            tvTenSach.setText(sach.getTenSach());
            tvTacGia.setText(sach.getTacGia());
            tvTheLoai.setText(sach.getTheLoai());
        }

        return convertView;
    }
}