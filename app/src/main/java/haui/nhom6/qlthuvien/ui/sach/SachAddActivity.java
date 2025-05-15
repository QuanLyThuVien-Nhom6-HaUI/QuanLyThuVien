package haui.nhom6.qlthuvien.ui.sach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import haui.nhom6.qlthuvien.MainActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.database.SachDAO;
import haui.nhom6.qlthuvien.model.Sach;

public class SachAddActivity extends AppCompatActivity {
    EditText edtMaSach, edtTenSach, edtTacGia, edtNhaXuatBan, edtNamXuatBan, edtTrangThai, edtTheLoai, edtGia, edtSoLuong;
    Button btnThem;
    ImageView icArrowBack, icUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sach_add);

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
        btnThem = findViewById(R.id.btnThem);
        icArrowBack = findViewById(R.id.icArrowBack);
        icUser = findViewById(R.id.icUser);

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

        btnThem.setOnClickListener(v -> {
            // Lấy dữ liệu từ các EditText
            String maSach = edtMaSach.getText().toString().trim();
            String tenSach = edtTenSach.getText().toString().trim();
            String tacGia = edtTacGia.getText().toString().trim();
            String nhaXuatBan = edtNhaXuatBan.getText().toString().trim();
            String namXuatBanStr = edtNamXuatBan.getText().toString().trim();
            String trangThai = edtTrangThai.getText().toString().trim();
            String theLoai = edtTheLoai.getText().toString().trim();
            String giaStr = edtGia.getText().toString().trim();
            String soLuongStr = edtSoLuong.getText().toString().trim();

            // Kiểm tra hợp lệ
            if (maSach.isEmpty()) {
                showToast("Mã sách không được để trống");
                return;
            }
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

            // Tạo đối tượng sách
            Sach sach = new Sach();
            sach.setMaSach(maSach);
            sach.setTenSach(tenSach);
            sach.setTacGia(tacGia);
            sach.setNhaXuatBan(nhaXuatBan);
            sach.setNamXuatBan(namXuatBan);
            sach.setTrangThai(trangThai);
            sach.setTheLoai(theLoai);
            sach.setGia(gia);
            sach.setSoLuong(soLuong);

            // Thêm sách vào cơ sở dữ liệu
            SachDAO sachDAO = new SachDAO(this);
            long result = sachDAO.themSach(sach);

            if (result > 0) {
                showToast("Đã thêm sách thành công!");
                finish();
            } else {
                showToast("Lỗi khi thêm sách, có thể mã sách đã tồn tại");
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}