package haui.nhom6.qlthuvien.ui.nhanvien;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import haui.nhom6.qlthuvien.AdminActivity;
import haui.nhom6.qlthuvien.MainActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.adapter.NhanVienAdapter;
import haui.nhom6.qlthuvien.model.NhanVien;

public class NhanVienActivity extends AppCompatActivity implements NhanVienContract.View {

    private ListView listViewNhanVien;
    private Button btnThemNhanVien;
    private EditText edtSearch;
    private ImageView icArrowBack, icUser;

    private NhanVienPresenter presenter;
    private NhanVienAdapter adapter;
    private List<NhanVien> fullList; // Dùng để lọc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhanvien);

        // Ánh xạ view
        listViewNhanVien = findViewById(R.id.listViewNhanVien);
        btnThemNhanVien = findViewById(R.id.btnThemNhanVien);
        edtSearch = findViewById(R.id.edt_search); // bạn cần thêm EditText này trong XML
        icArrowBack = findViewById(R.id.icArrowBack); // cần thêm ImageView này trong XML
        icUser = findViewById(R.id.icUser); // cần thêm ImageView này trong XML

        // Khởi tạo presenter
        presenter = new NhanVienPresenter(this, this);

        // Gọi load danh sách ban đầu
        presenter.loadNhanVienList();

        // Xử lý sự kiện nhấn Thêm nhân viên
        btnThemNhanVien.setOnClickListener(v -> {
            Intent intent = new Intent(this, NhanVienAddActivity.class);
            startActivity(intent);
        });

        // Xử lý back về AdminActivity
        icArrowBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý đăng xuất
        icUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Click 1 lần để xem chi tiết
        listViewNhanVien.setOnItemClickListener((parent, view, position, id) -> {
            NhanVien selectedNhanVien = (NhanVien) parent.getItemAtPosition(position);
            Intent intent = new Intent(NhanVienActivity.this, NhanVienDetailActivity.class);
            intent.putExtra("nhanvien", selectedNhanVien);
            startActivity(intent);
        });

        // Nhấn và giữ để xóa
        listViewNhanVien.setOnItemLongClickListener((parent, view, position, id) -> {
            NhanVien selectedNhanVien = (NhanVien) parent.getItemAtPosition(position);

            new AlertDialog.Builder(NhanVienActivity.this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá nhân viên này?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        presenter.deleteNhanVien(selectedNhanVien.getMaNhanVien());
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();

            return true;
        });

        // Thêm chức năng tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadNhanVienList();
    }

    @Override
    public void showNhanVienList(List<NhanVien> list) {
        fullList = new ArrayList<>(list);
        adapter = new NhanVienAdapter(this, list);
        listViewNhanVien.setAdapter(adapter);
    }

    private void filterList(String query) {
        if (fullList != null) {
            List<NhanVien> filteredList = new ArrayList<>();
            for (NhanVien nv : fullList) {
                if (nv.getTenNhanVien().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(nv);
                }
            }
            adapter = new NhanVienAdapter(this, filteredList);
            listViewNhanVien.setAdapter(adapter);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        presenter.loadNhanVienList();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
