package haui.nhom6.qlthuvien.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.model.SachDaChon;

import java.util.List;

public class SachDaChonAdapter extends RecyclerView.Adapter<SachDaChonAdapter.SachDaChonViewHolder> {
    private List<SachDaChon> sachDaChonList;
    private OnItemRemoveListener onItemRemoveListener;

    public SachDaChonAdapter(List<SachDaChon> sachDaChonList, OnItemRemoveListener onItemRemoveListener) {
        this.sachDaChonList = sachDaChonList;
        this.onItemRemoveListener = onItemRemoveListener;
    }

    @NonNull
    @Override
    public SachDaChonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sach_da_chon, parent, false);
        return new SachDaChonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SachDaChonViewHolder holder, int position) {
        SachDaChon sachDaChon = sachDaChonList.get(position);
        holder.tvTenSach.setText("Tên sách: " + sachDaChon.getSach().getTenSach());
        holder.tvTacGia.setText("Tác giả: " + sachDaChon.getSach().getTacGia());
        holder.etSoLuong.setText(String.valueOf(sachDaChon.getSoLuong()));
        holder.etSoLuong.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    int soLuong = Integer.parseInt(holder.etSoLuong.getText().toString());
                    if (soLuong <= 0) {
                        holder.etSoLuong.setText("1");
                        sachDaChon.setSoLuong(1);
                    } else if (soLuong > sachDaChon.getSach().getSoLuong()) {
                        holder.etSoLuong.setText(String.valueOf(sachDaChon.getSach().getSoLuong()));
                        sachDaChon.setSoLuong(sachDaChon.getSach().getSoLuong());
                    } else {
                        sachDaChon.setSoLuong(soLuong);
                    }
                } catch (NumberFormatException e) {
                    holder.etSoLuong.setText("1");
                    sachDaChon.setSoLuong(1);
                }
            }
        });
        holder.btnXoa.setOnClickListener(v -> onItemRemoveListener.onItemRemove(position));
    }

    @Override
    public int getItemCount() {
        return sachDaChonList.size();
    }

    public static class SachDaChonViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSach, tvTacGia;
        EditText etSoLuong;
        Button btnXoa;

        public SachDaChonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSach = itemView.findViewById(R.id.tvTenSach);
            tvTacGia = itemView.findViewById(R.id.tvTacGia);
            etSoLuong = itemView.findViewById(R.id.etSoLuong);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }

    public interface OnItemRemoveListener {
        void onItemRemove(int position);
    }
}