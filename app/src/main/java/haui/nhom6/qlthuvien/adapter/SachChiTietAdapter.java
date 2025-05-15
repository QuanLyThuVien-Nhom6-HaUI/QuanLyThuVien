package haui.nhom6.qlthuvien.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.model.SachChiTiet;

import java.util.List;

public class SachChiTietAdapter extends RecyclerView.Adapter<SachChiTietAdapter.SachChiTietViewHolder> {
    private List<SachChiTiet> sachChiTietList;

    public SachChiTietAdapter(List<SachChiTiet> sachChiTietList) {
        this.sachChiTietList = sachChiTietList;
    }

    @NonNull
    @Override
    public SachChiTietViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_sach_chi_tiet, parent, false);
        return new SachChiTietViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SachChiTietViewHolder holder, int position) {
        SachChiTiet sachChiTiet = sachChiTietList.get(position);
        holder.tvTenSach.setText("Tên sách: " + sachChiTiet.getTenSach());
        holder.tvTacGia.setText("Tác giả: " + sachChiTiet.getTenTacGia());
        holder.tvSoLuong.setText("Số lượng: " + sachChiTiet.getSoLuong());
        // Giả sử có imageResId, nếu không, dùng ảnh mặc định
        holder.ivSach.setImageResource(R.drawable.ic_book); // Thay bằng tài nguyên ảnh cụ thể nếu có
    }

    @Override
    public int getItemCount() {
        return sachChiTietList.size();
    }

    public static class SachChiTietViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSach;
        TextView tvTenSach, tvTacGia, tvSoLuong;

        public SachChiTietViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSach = itemView.findViewById(R.id.ivSach);
            tvTenSach = itemView.findViewById(R.id.tvTenSach);
            tvTacGia = itemView.findViewById(R.id.tvTacGia);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
        }
    }
}