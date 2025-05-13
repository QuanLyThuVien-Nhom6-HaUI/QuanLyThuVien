package haui.nhom6.qlthuvien;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import haui.nhom6.qlthuvien.ui.nguoidoc.NguoiDocActivity;

public class UserActivity extends AppCompatActivity {

    private Button btnNhanVien, btnNguoiDoc;
    private ImageView icUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btnNhanVien = findViewById(R.id.btnSach); // Giả sử dùng chung button "Quản lý sách" cho nhân viên
        btnNguoiDoc = findViewById(R.id.btnNguoiDoc);
        icUser = findViewById(R.id.icUser);

        icUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnNhanVien.setOnClickListener(v -> {
        });

        btnNguoiDoc.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, NguoiDocActivity.class);
            startActivity(intent);
        });
    }
}
