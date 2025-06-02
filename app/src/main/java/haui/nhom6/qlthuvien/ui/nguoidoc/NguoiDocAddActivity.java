package haui.nhom6.qlthuvien.ui.nguoidoc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import haui.nhom6.qlthuvien.MainActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.model.NguoiDoc;

public class NguoiDocAddActivity extends AppCompatActivity implements NguoiDocContract.View {
    private EditText edtMa, edtTen, edtCCCD, edtSDT, edtDiaChi;
    private RadioGroup radioGroupGioiTinh;
    private Button btnThem;
    private NguoiDocPresenter presenter;
    private ImageView icArrowBack, icUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoidoc_add);

        // Khởi tạo presenter
        presenter = new NguoiDocPresenter(this, this);

        edtMa = findViewById(R.id.edtMaNguoiDoc);
        edtTen = findViewById(R.id.edtTenNguoiDoc);
        edtCCCD = findViewById(R.id.edtCCCD);
        edtSDT = findViewById(R.id.edtSoDienThoai);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        radioGroupGioiTinh = findViewById(R.id.radioGroupGioiTinh);
        btnThem = findViewById(R.id.btnThem);
        icArrowBack = findViewById(R.id.icArrowBack);
        icUser = findViewById(R.id.icUser);

        icArrowBack.setOnClickListener(v -> finish());

        icUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnThem.setOnClickListener(v -> {
            String ma = edtMa.getText().toString().trim();
            String ten = edtTen.getText().toString().trim();
            String cccd = edtCCCD.getText().toString().trim();
            String sdt = edtSDT.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();

            int selectedId = radioGroupGioiTinh.getCheckedRadioButtonId();

            RadioButton selectedRadioButton = findViewById(selectedId);
            String gioiTinh = selectedRadioButton.getText().toString();

            if (ma.isEmpty() || ten.isEmpty() || cccd.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
                showToast("Vui lòng nhập đầy đủ thông tin");
                return;
            }
            if (ten.length() < 2) {
                showToast("Tên người đọc phải có ít nhất 2 ký tự");
                return;
            }
            if (diaChi.length() < 2) {
                showToast("Địa chỉ phải có ít nhất 2 ký tự");
                return;
            }

            if (sdt.length() != 10) {
                showToast("Số điện thoại không hợp lệ (Yêu cầu phải có 10 chữ số)");
                return;
            }

            if (cccd.length() != 12) {
                showToast("CCCD không hợp lệ (Yêu cầu phải có 12 chữ số)");
                return;
            }
            if (selectedId == -1) {
                showToast("Vui lòng chọn giới tính");
                return;
            }

            NguoiDoc nd = new NguoiDoc(ma, ten, cccd, sdt, gioiTinh, diaChi);
            presenter.addNguoiDoc(nd);
        });
    }

    @Override
    public void showNguoiDocList(List<NguoiDoc> list) {
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

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}