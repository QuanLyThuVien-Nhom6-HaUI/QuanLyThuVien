package haui.nhom6.qlthuvien.ui.phieumuon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import haui.nhom6.qlthuvien.AdminActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.UserActivity;
import haui.nhom6.qlthuvien.adapter.PhieuMuonAdapter;
import haui.nhom6.qlthuvien.database.PhieuMuonDAO;
import haui.nhom6.qlthuvien.model.PhieuMuon;

import java.util.List;

public class PhieuMuonActivity extends AppCompatActivity {
    private RecyclerView rvPhieuMuon;
    private PhieuMuonDAO phieuMuonDAO;
    private PhieuMuonAdapter phieuMuonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_muon);

        rvPhieuMuon = findViewById(R.id.rvPhieuMuon);
        phieuMuonDAO = new PhieuMuonDAO(this);

        // Load danh sách phiếu mượn
        List<PhieuMuon> phieuMuonList = phieuMuonDAO.getAllPhieuMuon();
        phieuMuonAdapter = new PhieuMuonAdapter(phieuMuonList);
        rvPhieuMuon.setLayoutManager(new LinearLayoutManager(this));
        rvPhieuMuon.setAdapter(phieuMuonAdapter);

        // Sự kiện nút thêm phiếu mượn
        findViewById(R.id.btnThemPhieuMuon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhieuMuonActivity.this, ThemPhieuMuonActivity.class));
            }
        });

        // Sự kiện nút quay lại
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy vai trò từ SharedPreferences
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String role = prefs.getString("role", "nhanvien"); // Mặc định là nhanvien nếu không có

                Intent intent;
                if (role.equals("quanly")) {
                    intent = new Intent(PhieuMuonActivity.this, AdminActivity.class);
                } else {
                    intent = new Intent(PhieuMuonActivity.this, UserActivity.class);
                }
                startActivity(intent);
                finish(); // Đóng PhieuMuonActivity
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật danh sách khi quay lại activity
        List<PhieuMuon> updatedList = phieuMuonDAO.getAllPhieuMuon();
        phieuMuonAdapter = new PhieuMuonAdapter(updatedList);
        rvPhieuMuon.setAdapter(phieuMuonAdapter);
    }
}