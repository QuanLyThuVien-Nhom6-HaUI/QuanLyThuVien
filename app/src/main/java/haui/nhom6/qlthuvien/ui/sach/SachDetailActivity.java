package haui.nhom6.qlthuvien.ui.sach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

import haui.nhom6.qlthuvien.MainActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.model.Sach;

public class SachDetailActivity extends AppCompatActivity implements SachContract.View {

    private EditText edtMaSach, edtTenSach, edtTacGia, edtNhaXuatBan, edtNamXuatBan, edtTrangThai, edtTheLoai, edtGia, edtSoLuong;
    private Button btnSua, btnXoa;
    private SachPresenter presenter;
    private Sach sach;
    private ImageView icArrowBack, icUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sach_detail);

        // Ánh xạ các view
        edtMaSach = findViewById(R.id.edtMaSach);
        edtTenSach = findViewById(R.id.edtTenSach);
        edtTacGia = findViewById(R.id.edtTacGia);
        edtNhaXuatBan = findViewById(R.id.edtNhaXuatBan);
        edtNamXuatBan = findViewById(R.id.edtNamXuatBan);
        edtTrangThai = findViewById(R.id.edtTrangThai);
        edtTheLoai = findViewById(R.id.edtTheLoai);
        edtGia = findViewById(R.id.edtGia);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        icArrowBack = findViewById(R.id.icArrowBack);
        icUser = findViewById(R.id.icUser);

        presenter = new SachPresenter(this, this);

        // Xử lý back navigation
        icArrowBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, SachActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý logout khi nhấn ic_user
        icUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Lấy dữ liệu sách từ Intent
        sach = (Sach) getIntent().getSerializableExtra("sach");
        if (sach == null) {
            showToast("Dữ liệu sách không hợp lệ");
            finish();
            return;
        }

        // Hiển thị dữ liệu sách
        edtMaSach.setText(sach.getMaSach());
        edtMaSach.setEnabled(false); // Không cho sửa mã sách
        edtTenSach.setText(sach.getTenSach());
        edtTacGia.setText(sach.getTacGia());
        edtNhaXuatBan.setText(sach.getNhaXuatBan());
        edtNamXuatBan.setText(String.valueOf(sach.getNamXuatBan()));
        edtTrangThai.setText(sach.getTrangThai());
        edtTheLoai.setText(sach.getTheLoai());
        edtGia.setText(String.valueOf(sach.getGia()));
        edtSoLuong.setText(String.valueOf(sach.getSoLuong()));

        btnSua.setOnClickListener(v -> {
            // Lấy dữ liệu từ các EditText
            String tenSach = edtTenSach.getText().toString().trim();
            String tacGia = edtTacGia.getText().toString().trim();
            String nhaXuatBan = edtNhaXuatBan.getText().toString().trim();
            String namXuatBanStr = edtNamXuatBan.getText().toString().trim();
            String trangThai = edtTrangThai.getText().toString().trim();
            String theLoai = edtTheLoai.getText().toString().trim();
            String giaStr = edtGia.getText().toString().trim();
            String soLuongStr = edtSoLuong.getText().toString().trim();

            // Kiểm tra hợp lệ
            if (tenSach.isEmpty()) {
                showToast("Tên sách không được để trống");
                return;
            }
            if (tacGia.isEmpty()) {
                showToast("Tác giả không được để trống");
                return;
            }
            if (nhaXuatBan.isEmpty()) {
                showToast("Nhà xuất bản không được để trống");
                return;
            }
            if (namXuatBanStr.isEmpty()) {
                showToast("Năm xuất bản không được để trống");
                return;
            }
            if (trangThai.isEmpty()) {
                showToast("Trạng thái không được để trống");
                return;
            }
            if (theLoai.isEmpty()) {
                showToast("Thể loại không được để trống");
                return;
            }
            if (giaStr.isEmpty()) {
                showToast("Giá không được để trống");
                return;
            }
            if (soLuongStr.isEmpty()) {
                showToast("Số lượng không được để trống");
                return;
            }

            // Kiểm tra định dạng số và giá trị hợp lệ
            int namXuatBan, gia, soLuong;
            int currentYear = Calendar.getInstance().get(Calendar.YEAR); // Lấy năm hiện tại
            try {
                namXuatBan = Integer.parseInt(namXuatBanStr);
                if (!namXuatBanStr.matches("\\d{4}")) {
                    showToast("Năm xuất bản phải là số 4 chữ số");
                    return;
                }
                if (namXuatBan < 1500 || namXuatBan > currentYear) {
                    showToast("Năm xuất bản phải từ 1500 đến " + currentYear);
                    return;
                }

                gia = Integer.parseInt(giaStr);
                if (!giaStr.matches("\\d+")) {
                    showToast("Giá phải là số nguyên");
                    return;
                }
                if (gia <= 0) {
                    showToast("Giá phải lớn hơn 0");
                    return;
                }

                soLuong = Integer.parseInt(soLuongStr);
                if (!soLuongStr.matches("\\d+")) {
                    showToast("Số lượng phải là số nguyên");
                    return;
                }
                if (soLuong < 0) {
                    showToast("Số lượng không được âm");
                    return;
                }
            } catch (NumberFormatException e) {
                showToast("Dữ liệu số không hợp lệ (năm, giá, hoặc số lượng)");
                return;
            }

            // Gán lại dữ liệu sau khi kiểm tra hợp lệ
            sach.setTenSach(tenSach);
            sach.setTacGia(tacGia);
            sach.setNhaXuatBan(nhaXuatBan);
            sach.setNamXuatBan(namXuatBan);
            sach.setTrangThai(trangThai);
            sach.setTheLoai(theLoai);
            sach.setGia(gia);
            sach.setSoLuong(soLuong);

            presenter.updateSach(sach);
        });

        btnXoa.setOnClickListener(v -> {
            new AlertDialog.Builder(SachDetailActivity.this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa sách này?")
                    .setPositiveButton("Xóa", (dialog, which) -> presenter.deleteSach(sach.getMaSach()))
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public void showSachList(List<Sach> list) {
        // Không cần xử lý trong màn chi tiết
    }

    @Override
    public void showError(String message) {
        showToast(message);
    }

    @Override
    public void onSuccess(String message) {
        showToast(message);
        finish();
    }

    @Override
    public void onError(String message) {
        showToast(message);
    }

    @Override
    public void updatePageInfo(int currentPage, int totalPages) {
        // Không cần xử lý trong màn chi tiết
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}