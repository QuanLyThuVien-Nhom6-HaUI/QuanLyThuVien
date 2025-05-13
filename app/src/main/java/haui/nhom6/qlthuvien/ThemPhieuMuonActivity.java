package haui.nhom6.qlthuvien;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import haui.nhom6.qlthuvien.database.NguoiDocDAO;
import haui.nhom6.qlthuvien.database.NhanVienDAO;
import haui.nhom6.qlthuvien.database.PhieuMuonDAO;
import haui.nhom6.qlthuvien.model.NguoiDoc;
import haui.nhom6.qlthuvien.model.NhanVien;
import haui.nhom6.qlthuvien.model.PhieuMuon;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ThemPhieuMuonActivity extends AppCompatActivity {
    private EditText etMaPhieu, etNgayMuon, etHanTraSach;
    private Spinner spinnerNhanVien, spinnerNguoiDoc;
    private PhieuMuonDAO phieuMuonDAO;
    private NhanVienDAO nhanVienDAO;
    private NguoiDocDAO nguoiDocDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_phieu_muon);

        etMaPhieu = findViewById(R.id.etMaPhieu);
        spinnerNhanVien = findViewById(R.id.spinnerNhanVien);
        spinnerNguoiDoc = findViewById(R.id.spinnerNguoiDoc);
        etNgayMuon = findViewById(R.id.etNgayMuon);
        etHanTraSach = findViewById(R.id.etHanTraSach);
        phieuMuonDAO = new PhieuMuonDAO(this);
        nhanVienDAO = new NhanVienDAO(this);
        nguoiDocDAO = new NguoiDocDAO(this);

        // Load danh sách nhân viên vào Spinner
        List<NhanVien> nhanVienList = nhanVienDAO.getAllNhanVien();
        ArrayAdapter<NhanVien> nhanVienAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nhanVienList);
        nhanVienAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNhanVien.setAdapter(nhanVienAdapter);

        // Load danh sách độc giả vào Spinner
        List<NguoiDoc> nguoiDocList = nguoiDocDAO.getAllNguoiDoc();
        ArrayAdapter<NguoiDoc> nguoiDocAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nguoiDocList);
        nguoiDocAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNguoiDoc.setAdapter(nguoiDocAdapter);

        // DatePicker cho ngày mượn
        etNgayMuon.setOnClickListener(v -> showDatePickerDialog(etNgayMuon));
        etHanTraSach.setOnClickListener(v -> showDatePickerDialog(etHanTraSach));

        findViewById(R.id.btnThem).setOnClickListener(v -> themPhieuMuon());
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    editText.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void themPhieuMuon() {
        String maPhieu = etMaPhieu.getText().toString();
        NhanVien nhanVien = (NhanVien) spinnerNhanVien.getSelectedItem();
        NguoiDoc nguoiDoc = (NguoiDoc) spinnerNguoiDoc.getSelectedItem();
        String ngayMuonStr = etNgayMuon.getText().toString();
        String hanTraSachStr = etHanTraSach.getText().toString();

        if (maPhieu.isEmpty() || nhanVien == null || nguoiDoc == null ||
                ngayMuonStr.isEmpty() || hanTraSachStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        PhieuMuon phieuMuon = new PhieuMuon();
        phieuMuon.setMaPhieu(maPhieu);
        phieuMuon.setMaNhanVien(nhanVien.getMaNhanVien());
        phieuMuon.setMaNguoiDoc(nguoiDoc.getMaNguoiDoc());
        try {
            phieuMuon.setNgayMuon(new SimpleDateFormat("yyyy-MM-dd").parse(ngayMuonStr));
            phieuMuon.setHanTraSach(new SimpleDateFormat("yyyy-MM-dd").parse(hanTraSachStr));
        } catch (Exception e) {
            Toast.makeText(this, "Định dạng ngày không hợp lệ (yyyy-MM-dd)", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = phieuMuonDAO.insertPhieuMuon(phieuMuon);
        if (result > 0) {
            Toast.makeText(this, "Thêm phiếu mượn thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm phiếu mượn thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}