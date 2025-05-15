package haui.nhom6.qlthuvien.ui.nhanvien;

import android.app.DatePickerDialog;
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
import haui.nhom6.qlthuvien.model.NhanVien;


public class NhanVienDetailActivity extends AppCompatActivity implements NhanVienContract.View {

    private EditText edtTenNV, edtNgaySinhNV, edtQueQuanNV, edtSDTNV;
    private Button btnSua, btnXoa;
    private ImageView icArrowBack, icUser;
    private NhanVienPresenter presenter;
    private NhanVien nhanVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhanvien_detail);

        edtTenNV = findViewById(R.id.edtTenNV);
        edtNgaySinhNV = findViewById(R.id.edtNgaySinhNV);
        edtQueQuanNV = findViewById(R.id.edtQueQuanNV);
        edtSDTNV = findViewById(R.id.edtSDTNV);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        icArrowBack = findViewById(R.id.icArrowBack);
        icUser = findViewById(R.id.icUser);

        presenter = new NhanVienPresenter(this, this);

        nhanVien = (NhanVien) getIntent().getSerializableExtra("nhanvien");

        edtNgaySinhNV.setOnClickListener(v -> showDatePickerDialog(edtNgaySinhNV));

        icArrowBack.setOnClickListener(v -> finish());

        icUser.setOnClickListener(v -> {
            Intent intent = new Intent(NhanVienDetailActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        if (nhanVien != null) {
            edtTenNV.setText(nhanVien.getTenNhanVien());
            edtNgaySinhNV.setText(nhanVien.getNgaySinh());
            edtQueQuanNV.setText(nhanVien.getQueQuan());
            edtSDTNV.setText(nhanVien.getSoDienThoai());
        }

        btnSua.setOnClickListener(v -> {
            String ten = edtTenNV.getText().toString().trim();
            String ngaySinh = edtNgaySinhNV.getText().toString().trim();
            String queQuan = edtQueQuanNV.getText().toString().trim();
            String sdt = edtSDTNV.getText().toString().trim();

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

            nhanVien.setTenNhanVien(ten);
            nhanVien.setNgaySinh(ngaySinh);
            nhanVien.setQueQuan(queQuan);
            nhanVien.setSoDienThoai(sdt);

            presenter.updateNhanVien(nhanVien);
        });

        btnXoa.setOnClickListener(v -> {
            new AlertDialog.Builder(NhanVienDetailActivity.this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá nhân viên này?")
                    .setPositiveButton("Xoá", (dialog, which) ->
                            presenter.deleteNhanVien(nhanVien.getMaNhanVien()))
                    .setNegativeButton("Huỷ", null)
                    .show();
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

    @Override
    public void showNhanVienList(List<NhanVien> list) {
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
