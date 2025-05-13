package haui.nhom6.qlthuvien;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import haui.nhom6.qlthuvien.adapter.SachChiTietAdapter;
import haui.nhom6.qlthuvien.database.DatabaseHelper;
import haui.nhom6.qlthuvien.model.PhieuMuon;
import haui.nhom6.qlthuvien.model.SachChiTiet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuMuonActivity extends AppCompatActivity {
    private TextView tvMaPhieu, tvTenNhanVien, tvTenNguoiDoc, tvNgayMuon, tvHanTra;
    private RecyclerView rvSachChiTiet;
    private SachChiTietAdapter sachChiTietAdapter;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phieu_muon);

        // Khởi tạo các view
        tvMaPhieu = findViewById(R.id.tvMaPhieu);
        tvTenNhanVien = findViewById(R.id.tvTenNhanVien);
        tvTenNguoiDoc = findViewById(R.id.tvTenNguoiDoc);
        tvNgayMuon = findViewById(R.id.tvNgayMuon);
        tvHanTra = findViewById(R.id.tvHanTra);
        rvSachChiTiet = findViewById(R.id.rvSachChiTiet);
        btnClose = findViewById(R.id.btnClose);

        dbHelper = new DatabaseHelper(this);

        // Nhận dữ liệu từ Intent
        PhieuMuon phieuMuon = (PhieuMuon) getIntent().getSerializableExtra("phieuMuon");
        if (phieuMuon == null) {
            Toast.makeText(this, "Không nhận được dữ liệu phiếu mượn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Toast.makeText(this, "Nhận được phiếu: " + phieuMuon.getMaPhieu(), Toast.LENGTH_SHORT).show();

        // Hiển thị thông tin cơ bản
        tvMaPhieu.setText("Mã phiếu: " + phieuMuon.getMaPhieu());
        tvTenNhanVien.setText("Nhân viên: " + (phieuMuon.getTenNhanVien() != null ? phieuMuon.getTenNhanVien() : "Chưa có"));
        tvTenNguoiDoc.setText("Người mượn: " + (phieuMuon.getTenNguoiDoc() != null ? phieuMuon.getTenNguoiDoc() : "Chưa có"));
        tvNgayMuon.setText("Bắt đầu: " + (phieuMuon.getNgayMuon() != null ? new SimpleDateFormat("dd/MM/yyyy").format(phieuMuon.getNgayMuon()) : "N/A"));
        tvHanTra.setText("Hạn trả: " + (phieuMuon.getHanTraSach() != null ? new SimpleDateFormat("dd/MM/yyyy").format(phieuMuon.getHanTraSach()) : "N/A"));

        // Lấy danh sách sách từ ChiTietPhieu và Sach
        List<SachChiTiet> sachChiTietList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT ct.maSach, ct.soLuong, s.tenSach, s.tacGia " +
                    "FROM ChiTietPhieu ct " +
                    "LEFT JOIN Sach s ON ct.maSach = s.maSach " +
                    "WHERE ct.maPhieu = ?";
            cursor = db.rawQuery(query, new String[]{phieuMuon.getMaPhieu()});

            int tenSachIndex = cursor.getColumnIndex("tenSach");
            int tacGiaIndex = cursor.getColumnIndex("tacGia");
            int soLuongIndex = cursor.getColumnIndex("soLuong");

            if (tenSachIndex == -1 || tacGiaIndex == -1 || soLuongIndex == -1) {
                Toast.makeText(this, "Cột không tồn tại: " +
                        (tenSachIndex == -1 ? "tenSach " : "") +
                        (tacGiaIndex == -1 ? "tacGia " : "") +
                        (soLuongIndex == -1 ? "soLuong" : ""), Toast.LENGTH_LONG).show();
            } else if (cursor.moveToFirst()) {
                do {
                    SachChiTiet sachChiTiet = new SachChiTiet();
                    String tenSach = cursor.getString(tenSachIndex);
                    String tacGia = cursor.getString(tacGiaIndex);
                    int soLuong = cursor.getInt(soLuongIndex);
                    sachChiTiet.setTenSach(tenSach != null ? tenSach : "Chưa có");
                    sachChiTiet.setTenTacGia(tacGia != null ? tacGia : "Chưa có");
                    sachChiTiet.setSoLuong(soLuong);
                    sachChiTietList.add(sachChiTiet);
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "Không tìm thấy sách cho phiếu: " + phieuMuon.getMaPhieu(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        // Hiển thị danh sách sách trong RecyclerView
        if (sachChiTietList.isEmpty()) {
            Toast.makeText(this, "Danh sách sách trống", Toast.LENGTH_SHORT).show();
        }
        sachChiTietAdapter = new SachChiTietAdapter(sachChiTietList);
        rvSachChiTiet.setLayoutManager(new LinearLayoutManager(this));
        rvSachChiTiet.setAdapter(sachChiTietAdapter);

        // Thêm sự kiện cho nút đóng
        btnClose.setOnClickListener(v -> finish());
    }
}