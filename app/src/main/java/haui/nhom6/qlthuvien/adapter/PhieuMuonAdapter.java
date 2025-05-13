package haui.nhom6.qlthuvien.adapter;

import android.content.Intent;
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
import java.util.List;

public class PhieuMuonAdapter extends RecyclerView.Adapter<PhieuMuonAdapter.PhieuMuonViewHolder> {
    private List<PhieuMuon> phieuMuonList;

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
        holder.tvNhanVien.setText("Nhân viên: " + phieuMuon.getTenNhanVien());
        holder.tvNguoiMuon.setText("Người mượn: " + phieuMuon.getTenNguoiDoc());
        holder.tvNgayMuon.setText("Bắt đầu: " + (phieuMuon.getNgayMuon() != null ? new SimpleDateFormat("dd/MM/yyyy").format(phieuMuon.getNgayMuon()) : "N/A"));
        holder.tvHanTra.setText("Hạn trả: " + (phieuMuon.getHanTraSach() != null ? new SimpleDateFormat("dd/MM/yyyy").format(phieuMuon.getHanTraSach()) : "N/A"));

        // Thêm sự kiện nhấn
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Nhấn vào phiếu: " + phieuMuon.getMaPhieu(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), ChiTietPhieuMuonActivity.class);
            intent.putExtra("phieuMuon", phieuMuon); // Bây giờ sẽ hoạt động vì PhieuMuon implements Serializable
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