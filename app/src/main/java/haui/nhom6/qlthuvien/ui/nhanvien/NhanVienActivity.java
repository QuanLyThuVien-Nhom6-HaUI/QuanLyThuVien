package haui.nhom6.qlthuvien.ui.nhanvien;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.adapter.NhanVienAdapter;
import haui.nhom6.qlthuvien.model.NhanVien;

public class NhanVienActivity extends AppCompatActivity implements NhanVienContract.View {

    private ListView listViewNhanVien;
    private Button btnThemNhanVien;
    private NhanVienPresenter presenter;
    private NhanVienAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhanvien);

        // Ánh xạ view
        listViewNhanVien = findViewById(R.id.listViewNhanVien);
        btnThemNhanVien = findViewById(R.id.btnThemNhanVien);

        // Khởi tạo presenter
        presenter = new NhanVienPresenter(this, this);

        // Gọi load danh sách ban đầu
        presenter.loadNhanVienList();

        // Xử lý sự kiện nhấn Thêm nhân viên
        btnThemNhanVien.setOnClickListener(v -> {
            Intent intent = new Intent(this, NhanVienAddActivity.class);
            startActivity(intent);
        });

        // Click 1 lần để xem chi tiết
        listViewNhanVien.setOnItemClickListener((parent, view, position, id) -> {
            NhanVien selectedNhanVien = (NhanVien) parent.getItemAtPosition(position);
            Intent intent = new Intent(NhanVienActivity.this, NhanVienDetailActivity.class);
            intent.putExtra("nhanvien", selectedNhanVien);
            startActivity(intent);
        });

        // Nhấn và giữ để xóa
        listViewNhanVien.setOnItemLongClickListener((parent, view, position, id) -> {
            NhanVien selectedNhanVien = (NhanVien) parent.getItemAtPosition(position);

            new AlertDialog.Builder(NhanVienActivity.this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá nhân viên này?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        presenter.deleteNhanVien(selectedNhanVien.getMaNhanVien());
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();

            return true; // báo là đã xử lý long click
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload danh sách mỗi khi quay lại từ màn hình khác
        presenter.loadNhanVienList();
    }

    @Override
    public void showNhanVienList(List<NhanVien> list) {
        adapter = new NhanVienAdapter(this, list);
        listViewNhanVien.setAdapter(adapter);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        presenter.loadNhanVienList(); // Cập nhật lại danh sách sau khi xoá
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
