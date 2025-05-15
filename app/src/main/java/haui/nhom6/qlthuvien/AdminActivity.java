package haui.nhom6.qlthuvien;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.ui.nguoidoc.NguoiDocActivity;
import haui.nhom6.qlthuvien.ui.nhanvien.NhanVienActivity;
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
//        btnSach.setOnClickListener(v -> {
//            Intent intent = new Intent(AdminActivity.this, SachActivity.class);
//            startActivity(intent);
//        });

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
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}