package haui.nhom6.qlthuvien.ui.nguoidoc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import haui.nhom6.qlthuvien.MainActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.model.NguoiDoc;

public class NguoiDocDetailActivity extends AppCompatActivity implements NguoiDocContract.View {

    private EditText edtTen, edtCCCD, edtSDT, edtDiaChi;
    private RadioGroup radioGroupGioiTinh;
    private RadioButton radioNam, radioNu;
    private Button btnSua, btnXoa;
    private ImageView icArrowBack, icUser;
    private NguoiDocPresenter presenter;
    private NguoiDoc nguoiDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoidoc_detail);

        edtTen = findViewById(R.id.edtTenNguoiDoc);
        edtCCCD = findViewById(R.id.edtCCCD);
        edtSDT = findViewById(R.id.edtSoDienThoai);
        edtDiaChi = findViewById(R.id.edtDiaChi);

        radioGroupGioiTinh = findViewById(R.id.radioGroupGioiTinh);
        radioNam = findViewById(R.id.radioNam);
        radioNu = findViewById(R.id.radioNu);

        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        icArrowBack = findViewById(R.id.icArrowBack);
        icUser = findViewById(R.id.icUser);

        presenter = new NguoiDocPresenter(this, this);
        nguoiDoc = (NguoiDoc) getIntent().getSerializableExtra("nguoidoc");

        if (nguoiDoc != null) {
            edtTen.setText(nguoiDoc.getTenNguoiDoc());
            edtCCCD.setText(nguoiDoc.getCccd());
            edtSDT.setText(nguoiDoc.getSoDienThoai());
            edtDiaChi.setText(nguoiDoc.getDiaChi());
            Log.d("GioiTinh", "Giới tính từ DB: " + nguoiDoc.getGioiTinh());

            if ("Nam".equalsIgnoreCase(nguoiDoc.getGioiTinh())) {
                radioNam.setChecked(true);
            } else if ("Nu".equalsIgnoreCase(nguoiDoc.getGioiTinh())) {
                radioNu.setChecked(true);
            }
            } else if ("Nữ".equalsIgnoreCase(nguoiDoc.getGioiTinh())) {
                radioNu.setChecked(true);
            }
         else {
            showToast("Không thể tải thông tin người đọc");
            finish();
        }

        icArrowBack.setOnClickListener(v -> finish());

        icUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnSua.setOnClickListener(v -> {
            String ten = edtTen.getText().toString().trim();
            String cccd = edtCCCD.getText().toString().trim();
            String sdt = edtSDT.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();

            int selectedId = radioGroupGioiTinh.getCheckedRadioButtonId();
            if (selectedId == -1) {
                showToast("Vui lòng chọn giới tính");
                return;
            }
            RadioButton selectedRadio = findViewById(selectedId);
            String gioiTinh = selectedRadio.getText().toString();
            if (ten.isEmpty()) {
                showToast("Tên người đọc không được để trống");
                return;
            }
            if (ten.length() < 2) {
                showToast("Tên người đọc phải có ít nhất 2 ký tự");
                return;
            }
            if (cccd.isEmpty()) {
                showToast("CCCD không được để trống");
                return;
            }
            if (!cccd.matches("^\\d{12}$")) {
                showToast("CCCD phải đủ 12 số");
                return;
            }
            if (sdt.isEmpty()) {
                showToast("Số điện thoại không được để trống");
                return;
            }
            if (!sdt.matches("^\\d{10}$")) {
                showToast("Số điện thoại phải có 10 chữ số");
                return;
            }
            if (diaChi.length() < 2) {
                showToast("Địa chỉ phải có ít nhất 2 ký tự");
                return;
            }


            nguoiDoc.setTenNguoiDoc(ten);
            nguoiDoc.setCccd(cccd);
            nguoiDoc.setSoDienThoai(sdt);
            nguoiDoc.setGioiTinh(gioiTinh);
            nguoiDoc.setDiaChi(diaChi);
            presenter.updateNguoiDoc(nguoiDoc);
        });

        btnXoa.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá người đọc này?")
                    .setPositiveButton("Xoá", (dialog, which) ->
                            presenter.deleteNguoiDoc(nguoiDoc.getMaNguoiDoc()))
                    .setNegativeButton("Huỷ", null)
                    .show();
        });
    }

    @Override
    public void showNguoiDocList(List<NguoiDoc> list) {}

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
