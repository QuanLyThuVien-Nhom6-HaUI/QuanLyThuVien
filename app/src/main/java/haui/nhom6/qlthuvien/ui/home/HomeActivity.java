package haui.nhom6.qlthuvien.ui.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.ui.nhanvien.NhanVienActivity;
import haui.nhom6.qlthuvien.ui.sach.SachActivity;
import haui.nhom6.qlthuvien.ui.thongke.ThongKeActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btnNhanVien).setOnClickListener(v -> {
            startActivity(new Intent(this, NhanVienActivity.class));
        });

        findViewById(R.id.btnSach).setOnClickListener(v -> {
            startActivity(new Intent(this, SachActivity.class));
        });

        findViewById(R.id.btnThongKe).setOnClickListener(v -> {
            startActivity(new Intent(this, ThongKeActivity.class));
        });
    }
}