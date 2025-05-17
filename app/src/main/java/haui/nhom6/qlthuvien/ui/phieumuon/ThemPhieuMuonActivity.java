package haui.nhom6.qlthuvien.ui.phieumuon;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.adapter.SachDaChonAdapter;
import haui.nhom6.qlthuvien.database.ChiTietPhieuDAO;
import haui.nhom6.qlthuvien.database.NguoiDocDAO;
import haui.nhom6.qlthuvien.database.NhanVienDAO;
import haui.nhom6.qlthuvien.database.PhieuMuonDAO;
import haui.nhom6.qlthuvien.database.SachDAO;
import haui.nhom6.qlthuvien.model.NguoiDoc;
import haui.nhom6.qlthuvien.model.NhanVien;
import haui.nhom6.qlthuvien.model.PhieuMuon;
import haui.nhom6.qlthuvien.model.Sach;
import haui.nhom6.qlthuvien.model.SachDaChon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThemPhieuMuonActivity extends AppCompatActivity {
    private EditText etMaPhieu, etNgayMuon, etHanTraSach;
    private Spinner spinnerNhanVien, spinnerNguoiDoc, spinnerSach;
    private RecyclerView rvSachDaChon;
    private PhieuMuonDAO phieuMuonDAO;
    private NhanVienDAO nhanVienDAO;
    private NguoiDocDAO nguoiDocDAO;
    private SachDAO sachDAO;
    private ChiTietPhieuDAO chiTietPhieuDAO;
    private List<SachDaChon> sachDaChonList;
    private SachDaChonAdapter sachDaChonAdapter;
    private List<Sach> sachList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_phieu_muon);

        etMaPhieu = findViewById(R.id.etMaPhieu);
        spinnerNhanVien = findViewById(R.id.spinnerNhanVien);
        spinnerNguoiDoc = findViewById(R.id.spinnerNguoiDoc);
        spinnerSach = findViewById(R.id.spinnerSach);
        etNgayMuon = findViewById(R.id.etNgayMuon);
        etHanTraSach = findViewById(R.id.etHanTraSach);
        rvSachDaChon = findViewById(R.id.rvSachDaChon);

        phieuMuonDAO = new PhieuMuonDAO(this);
        nhanVienDAO = new NhanVienDAO(this);
        nguoiDocDAO = new NguoiDocDAO(this);
        sachDAO = new SachDAO(this);
        chiTietPhieuDAO = new ChiTietPhieuDAO(this);
        sachDaChonList = new ArrayList<>();

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

        // Load danh sách sách vào Spinner
        sachList = sachDAO.getAllSach();
        List<String> tenSachList = new ArrayList<>();
        tenSachList.add("Chọn sách");
        for (Sach s : sachList) {
            tenSachList.add(s.getTenSach() + " - " + s.getTacGia());
        }
        ArrayAdapter<String> sachAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenSachList);
        sachAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSach.setAdapter(sachAdapter);

        // Thiết lập RecyclerView cho sách đã chọn
        sachDaChonAdapter = new SachDaChonAdapter(sachDaChonList, position -> {
            sachDaChonList.remove(position);
            sachDaChonAdapter.notifyDataSetChanged();
        });
        rvSachDaChon.setLayoutManager(new LinearLayoutManager(this));
        rvSachDaChon.setAdapter(sachDaChonAdapter);

        // Sự kiện chọn sách
        spinnerSach.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return; // Bỏ qua nếu chọn "Chọn sách"
                Sach sach = sachList.get(position - 1);
                boolean alreadyAdded = false;
                for (SachDaChon s : sachDaChonList) {
                    if (s.getSach().getMaSach().equals(sach.getMaSach())) {
                        alreadyAdded = true;
                        break;
                    }
                }
                if (!alreadyAdded) {
                    if (sach.getSoLuong() <= 0) {
                        Toast.makeText(ThemPhieuMuonActivity.this, "Sách " + sach.getTenSach() + " đã hết!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sachDaChonList.add(new SachDaChon(sach, 1));
                    sachDaChonAdapter.notifyDataSetChanged();
                }
                spinnerSach.setSelection(0); // Reset Spinner
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // DatePicker cho ngày mượn và hạn trả
        etNgayMuon.setOnClickListener(v -> showDatePickerDialog(etNgayMuon));
        etHanTraSach.setOnClickListener(v -> showDatePickerDialog(etHanTraSach));

        // Sự kiện nút Thêm phiếu mượn
        findViewById(R.id.btnThem).setOnClickListener(v -> themPhieuMuon());

        // Sự kiện nút Đóng
        findViewById(R.id.btnDong).setOnClickListener(v -> {
            Intent intent = new Intent(ThemPhieuMuonActivity.this, PhieuMuonActivity.class);
            startActivity(intent);
            finish(); // Đóng activity hiện tại
        });
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

        if (sachDaChonList.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một cuốn sách", Toast.LENGTH_SHORT).show();
            return;
        }

        PhieuMuon phieuMuon = new PhieuMuon();
        phieuMuon.setMaPhieu(maPhieu);
        phieuMuon.setMaNhanVien(nhanVien.getMaNhanVien());
        phieuMuon.setMaNguoiDoc(nguoiDoc.getMaNguoiDoc());
        try {
            phieuMuon.setNgayMuon(dateFormat.parse(ngayMuonStr));
            phieuMuon.setHanTraSach(dateFormat.parse(hanTraSachStr));
        } catch (Exception e) {
            Toast.makeText(this, "Định dạng ngày không hợp lệ (yyyy-MM-dd)", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = phieuMuonDAO.insertPhieuMuon(phieuMuon);
        if (result > 0) {
            // Lưu chi tiết phiếu mượn
            for (SachDaChon sachDaChon : sachDaChonList) {
                chiTietPhieuDAO.insertChiTietPhieu(maPhieu, sachDaChon.getSach().getMaSach(), sachDaChon.getSoLuong());
                // Cập nhật số lượng sách
                int soLuongMoi = sachDaChon.getSach().getSoLuong() - sachDaChon.getSoLuong();
                sachDAO.updateSoLuongSach(sachDaChon.getSach().getMaSach(), soLuongMoi);
            }
            Toast.makeText(this, "Thêm phiếu mượn thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ThemPhieuMuonActivity.this, PhieuMuonActivity.class);
            startActivity(intent);
            finish(); // Quay lại danh sách sau khi thêm thành công
        } else {
            Toast.makeText(this, "Thêm phiếu mượn thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}