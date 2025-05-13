package haui.nhom6.qlthuvien.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.ui.nhanvien.NhanVienActivity;
import haui.nhom6.qlthuvien.ui.thongke.ThongKeActivity;

public class HomeActivity extends AppCompatActivity {
    Button btnNhanVien, btnThongKe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnNhanVien = findViewById(R.id.btnNhanVien);
        btnThongKe = findViewById(R.id.btnThongKe);
        btnNhanVien.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, NhanVienActivity.class);
            startActivity(intent);
        });

        btnThongKe.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ThongKeActivity.class);
            startActivity(intent);
        });
    }
}
