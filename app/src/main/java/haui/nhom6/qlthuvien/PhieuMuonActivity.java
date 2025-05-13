package haui.nhom6.qlthuvien;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import haui.nhom6.qlthuvien.adapter.PhieuMuonAdapter;
import haui.nhom6.qlthuvien.database.PhieuMuonDAO;
import haui.nhom6.qlthuvien.model.PhieuMuon;

import java.util.List;

public class PhieuMuonActivity extends AppCompatActivity {
    private RecyclerView rvPhieuMuon;
    private PhieuMuonAdapter adapter;
    private PhieuMuonDAO phieuMuonDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_muon);

        rvPhieuMuon = findViewById(R.id.rvPhieuMuon);
        phieuMuonDAO = new PhieuMuonDAO(this);

        // Lấy danh sách phiếu mượn
        List<PhieuMuon> phieuMuonList = phieuMuonDAO.getAllPhieuMuon();
        adapter = new PhieuMuonAdapter(phieuMuonList);
        rvPhieuMuon.setLayoutManager(new LinearLayoutManager(this));
        rvPhieuMuon.setAdapter(adapter);

        // Nút thêm phiếu mượn
        findViewById(R.id.btnThemPhieuMuon).setOnClickListener(v -> {
            startActivity(new Intent(this, ThemPhieuMuonActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Làm mới danh sách khi quay lại
        List<PhieuMuon> updatedList = phieuMuonDAO.getAllPhieuMuon();
        adapter = new PhieuMuonAdapter(updatedList);
        rvPhieuMuon.setAdapter(adapter);
    }
}