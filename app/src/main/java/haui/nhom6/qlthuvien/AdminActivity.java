package haui.nhom6.qlthuvien;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import haui.nhom6.qlthuvien.ui.nguoidoc.NguoiDocActivity;
import haui.nhom6.qlthuvien.ui.nhanvien.NhanVienActivity;
import haui.nhom6.qlthuvien.ui.phieumuon.PhieuMuonActivity;
import haui.nhom6.qlthuvien.ui.sach.SachActivity;
import haui.nhom6.qlthuvien.ui.thongke.ThongKeActivity;

public class AdminActivity extends AppCompatActivity {

    private Button btnNhanVien, btnNguoiDoc, btnSach, btnPhieuMuon, btnThongKe;
    private ImageView icUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize views
        btnNhanVien = findViewById(R.id.btnNhanVien);
        btnNguoiDoc = findViewById(R.id.btnNguoiDoc);
        btnSach = findViewById(R.id.btnSach);
        btnPhieuMuon = findViewById(R.id.btnPhieuMuon);
        btnThongKe = findViewById(R.id.btnThongKe);
        icUser = findViewById(R.id.icUser);

        // Xử lý sự kiện nhấn nút Quản lý nhân viên
        btnNhanVien.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, NhanVienActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện nhấn nút Quản lý người đọc
        btnNguoiDoc.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, NguoiDocActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện nhấn nút Quản lý sách
        btnSach.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, SachActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện nhấn nút Quản lý phiếu mượn
        btnPhieuMuon.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, PhieuMuonActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện nhấn nút Thống kê
        btnThongKe.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ThongKeActivity.class);
            startActivity(intent);
        });

        // Xử lý logout khi nhấn ic_user
        icUser.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Xóa vai trò khỏi SharedPreferences
                        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove("role");
                        editor.apply();

                        // Chuyển về màn hình đăng nhập
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .show();
        });

    }
}