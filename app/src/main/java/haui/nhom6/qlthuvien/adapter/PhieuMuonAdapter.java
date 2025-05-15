package haui.nhom6.qlthuvien.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import haui.nhom6.qlthuvien.ChiTietPhieuMuonActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.model.PhieuMuon;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PhieuMuonAdapter extends RecyclerView.Adapter<PhieuMuonAdapter.PhieuMuonViewHolder> {
    private List<PhieuMuon> phieuMuonList;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public PhieuMuonAdapter(List<PhieuMuon> phieuMuonList) {
        this.phieuMuonList = phieuMuonList;
    }

    @NonNull
    @Override
    public PhieuMuonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phieu_muon, parent, false);
        return new PhieuMuonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhieuMuonViewHolder holder, int position) {
        PhieuMuon phieuMuon = phieuMuonList.get(position);
        holder.tvNhanVien.setText("Nhân viên: " + (phieuMuon.getTenNhanVien() != null ? phieuMuon.getTenNhanVien() : "N/A"));
        holder.tvNguoiMuon.setText("Người mượn: " + (phieuMuon.getTenNguoiDoc() != null ? phieuMuon.getTenNguoiDoc() : "N/A"));
        holder.tvNgayMuon.setText("Bắt đầu: " + (phieuMuon.getNgayMuon() != null ? dateFormat.format(phieuMuon.getNgayMuon()) : "N/A"));
        holder.tvHanTra.setText("Hạn trả: " + (phieuMuon.getHanTraSach() != null ? dateFormat.format(phieuMuon.getHanTraSach()) : "N/A"));

        // Kiểm tra ngày hạn trả và đổi màu nếu cần
        if (phieuMuon.getHanTraSach() != null) {
            // Lấy ngày hiện tại tự động từ hệ thống
            Calendar currentDate = Calendar.getInstance();
            // Đặt thời gian về đầu ngày để so sánh chính xác
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.MILLISECOND, 0);

            // Chuyển ngày hạn trả thành Calendar
            Calendar hanTraDate = Calendar.getInstance();
            hanTraDate.setTime(phieuMuon.getHanTraSach());
            hanTraDate.set(Calendar.HOUR_OF_DAY, 0);
            hanTraDate.set(Calendar.MINUTE, 0);
            hanTraDate.set(Calendar.SECOND, 0);
            hanTraDate.set(Calendar.MILLISECOND, 0);

            // Tính khoảng cách giữa ngày hiện tại và ngày hạn trả (số ngày)
            long diffInMillies = hanTraDate.getTimeInMillis() - currentDate.getTimeInMillis();
            long daysDiff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            // Nếu ngày hạn trả còn 1 hoặc 2 ngày trong tương lai, đổi màu đỏ
            if (daysDiff > 0 && daysDiff <= 2) {
                holder.tvHanTra.setTextColor(Color.RED);
            } else {
                holder.tvHanTra.setTextColor(Color.BLACK); // Màu mặc định
            }
        } else {
            holder.tvHanTra.setTextColor(Color.BLACK); // Nếu không có ngày hạn trả, để màu mặc định
        }

        // Thêm sự kiện nhấn để xem chi tiết
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Nhấn vào phiếu: " + phieuMuon.getMaPhieu(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), ChiTietPhieuMuonActivity.class);
            intent.putExtra("phieuMuon", phieuMuon);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return phieuMuonList.size();
    }

    public static class PhieuMuonViewHolder extends RecyclerView.ViewHolder {
        TextView tvNhanVien, tvNguoiMuon, tvNgayMuon, tvHanTra;

        public PhieuMuonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNhanVien = itemView.findViewById(R.id.tvTenNhanVien);
            tvNguoiMuon = itemView.findViewById(R.id.tvTenNguoiDoc);
            tvNgayMuon = itemView.findViewById(R.id.tvNgayMuon);
            tvHanTra = itemView.findViewById(R.id.tvHanTra);
        }
    }
}