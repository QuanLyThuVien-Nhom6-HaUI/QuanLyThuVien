package haui.nhom6.qlthuvien.ui.nhanvien;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import haui.nhom6.qlthuvien.AdminActivity;
import haui.nhom6.qlthuvien.MainActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.database.NhanVienDAO;
import haui.nhom6.qlthuvien.model.NhanVien;


public class NhanVienAddActivity extends AppCompatActivity {

    EditText edtTen, edtNgaySinh, edtQueQuan, edtSDT,edtMa;
    Button btnThem;
    ImageView icArrowBack, icUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhanvien_add);
        edtMa=findViewById(R.id.edtMaNhanVien);
        edtTen = findViewById(R.id.edtTenNhanVien);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtQueQuan = findViewById(R.id.edtQueQuan);
        edtSDT = findViewById(R.id.edtSoDienThoai);
        btnThem = findViewById(R.id.btnThem);
        icArrowBack = findViewById(R.id.icArrowBack);
        icUser = findViewById(R.id.icUser);

        edtNgaySinh.setOnClickListener(v -> showDatePickerDialog(edtNgaySinh));

        icArrowBack.setOnClickListener(v -> finish());

        icUser.setOnClickListener(v -> {
            Intent intent = new Intent(NhanVienAddActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnThem.setOnClickListener(v -> {
            String ma = edtMa.getText().toString().trim();
            String ten = edtTen.getText().toString().trim();
            String ngaySinh = edtNgaySinh.getText().toString().trim();
            String queQuan = edtQueQuan.getText().toString().trim();
            String sdt = edtSDT.getText().toString().trim();
            if (ma.isEmpty()) {
                showToast("Mã nhân viên không được để trống");
                return;
            }
            if (ma.length() < 3) {
                showToast("Mã nhân viên phải có ít nhất 3 ký tự");
                return;
            }


            if (ten.isEmpty()) {
                showToast("Tên nhân viên không được để trống");
                return;
            }
            if (ten.length() < 3) {
                showToast("Tên nhân viên phải có ít nhất 3 ký tự");
                return;
            }
            if (ngaySinh.isEmpty()) {
                showToast("Ngày sinh không được để trống");
                return;
            }
            if (queQuan.isEmpty()) {
                showToast("Quê quán không được để trống");
                return;
            }
            if (sdt.isEmpty()) {
                showToast("Số điện thoại không được để trống");
                return;
            }
            if (!sdt.matches("^\\d{9,11}$")) {
                showToast("Số điện thoại không hợp lệ (phải là số và từ 9–11 chữ số)");
                return;
            }

            NhanVien nv = new NhanVien();
            nv.setMaNhanVien(ma);
            nv.setTenNhanVien(ten);
            nv.setNgaySinh(ngaySinh);
            nv.setQueQuan(queQuan);
            nv.setSoDienThoai(sdt);

            NhanVienDAO nhanVienDAO = new NhanVienDAO(this);
            long result = nhanVienDAO.themNhanVien(nv);

            if (result > 0) {
                showToast("Đã thêm nhân viên!");
            } else {
                showToast("Lỗi khi thêm nhân viên");
            }

            finish();
        });
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    editText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
