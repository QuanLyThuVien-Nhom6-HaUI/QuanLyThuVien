package haui.nhom6.qlthuvien;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import haui.nhom6.qlthuvien.ui.nguoidoc.NguoiDocActivity;
import haui.nhom6.qlthuvien.ui.phieumuon.PhieuMuonActivity;
import haui.nhom6.qlthuvien.ui.sach.SachActivity;

public class UserActivity extends AppCompatActivity {

    private Button btnNhanVien, btnNguoiDoc, btnPhieuMuon1, btnSach;
    private ImageView icUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btnNhanVien = findViewById(R.id.btnSach); // Giả sử dùng chung button "Quản lý sách" cho nhân viên
        btnNguoiDoc = findViewById(R.id.btnNguoiDoc);
        btnPhieuMuon1 = findViewById(R.id.btnPhieuMuon1);
        icUser = findViewById(R.id.icUser);
        btnSach = findViewById(R.id.btnSach);

        icUser.setOnClickListener(v -> {
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
        });

        btnNhanVien.setOnClickListener(v -> {
        });

        btnSach.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, SachActivity.class);
            startActivity(intent);
        });


        btnNguoiDoc.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, NguoiDocActivity.class);
            startActivity(intent);
        });

        btnPhieuMuon1.setOnClickListener(v ->{
            Intent intent = new Intent(UserActivity.this, PhieuMuonActivity.class);
            startActivity(intent);
        });
    }
}