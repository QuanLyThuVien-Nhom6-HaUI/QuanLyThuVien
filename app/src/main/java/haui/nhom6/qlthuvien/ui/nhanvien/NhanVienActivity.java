package haui.nhom6.qlthuvien.ui.nhanvien;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
    private Button btnThemNhanVien, btnPrevious, btnNext;
    private EditText edtSearch;
    private ImageView icArrowBack, icUser;
    private TextView tvPageNumber;

    private NhanVienPresenter presenter;
    private NhanVienAdapter adapter;

    private List<NhanVien> originalList = new ArrayList<>();
    private List<NhanVien> fullList = new ArrayList<>();
    private List<NhanVien> currentList = new ArrayList<>();

    private int currentPage = 1;
    private final int itemsPerPage = 10;
    private int totalPages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhanvien);

        // Ánh xạ view
        listViewNhanVien = findViewById(R.id.listViewNhanVien);
        btnThemNhanVien = findViewById(R.id.btnThemNhanVien);
        edtSearch = findViewById(R.id.edt_search);
        icArrowBack = findViewById(R.id.icArrowBack);
        icUser = findViewById(R.id.icUser);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        tvPageNumber = findViewById(R.id.tvPageNumber);

        // Khởi tạo presenter
        presenter = new NhanVienPresenter(this, this);

        // Load dữ liệu
        presenter.loadNhanVienList();

        // Sự kiện thêm nhân viên
        btnThemNhanVien.setOnClickListener(v -> {
            Intent intent = new Intent(this, NhanVienAddActivity.class);
            startActivity(intent);
        });

        // Quay về trang admin
        icArrowBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            finish();
        });

        // Quay về trang chính
        icUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Xem chi tiết nhân viên
        listViewNhanVien.setOnItemClickListener((parent, view, position, id) -> {
            NhanVien selected = (NhanVien) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, NhanVienDetailActivity.class);
            intent.putExtra("nhanvien", selected);
            startActivity(intent);
        });

        // Xoá nhân viên
        listViewNhanVien.setOnItemLongClickListener((parent, view, position, id) -> {
            NhanVien selected = (NhanVien) parent.getItemAtPosition(position);
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá nhân viên này?")
                    .setPositiveButton("Xoá", (dialog, which) -> presenter.deleteNhanVien(selected.getMaNhanVien()))
                    .setNegativeButton("Huỷ", null)
                    .show();
            return true;
        });

        // Tìm kiếm nhân viên
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Phân trang
        btnPrevious.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                updateListView();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                updateListView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadNhanVienList();
    }

    @Override
    public void showNhanVienList(List<NhanVien> list) {
        originalList = new ArrayList<>(list);     // Lưu danh sách gốc
        fullList = new ArrayList<>(list);         // Danh sách hiện tại để phân trang
        totalPages = (int) Math.ceil((double) fullList.size() / itemsPerPage);
        currentPage = 1;
        updateListView();
    }

    private void updateListView() {

        int start = (currentPage - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, fullList.size());
        currentList = fullList.subList(start, end);
        adapter = new NhanVienAdapter(this, currentList, currentPage, itemsPerPage); // <-- Sửa ở đây
        listViewNhanVien.setAdapter(adapter);
        tvPageNumber.setText("Trang " + currentPage + "/" + totalPages);


        // Cập nhật trạng thái và màu nút "Trước"
        if (currentPage > 1) {
            btnPrevious.setEnabled(true);
            btnPrevious.setBackgroundTintList(getColorStateList(android.R.color.black));
        } else {
            btnPrevious.setEnabled(false);
            btnPrevious.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
        }

        // Cập nhật trạng thái và màu nút "Sau"
        if (currentPage < totalPages) {
            btnNext.setEnabled(true);
            btnNext.setBackgroundTintList(getColorStateList(android.R.color.black));
        } else {
            btnNext.setEnabled(false);
            btnNext.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
        }
    }


    private void filterList(String query) {
        if (query.isEmpty()) {
            fullList = new ArrayList<>(originalList);  // Khôi phục danh sách gốc
        } else {
            List<NhanVien> filtered = new ArrayList<>();
            for (NhanVien nv : originalList) {
                if (nv.getTenNhanVien().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(nv);
                }
            }
            fullList = filtered;
        }

        totalPages = (int) Math.ceil((double) fullList.size() / itemsPerPage);
        currentPage = 1;
        updateListView();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        presenter.loadNhanVienList(); // Load lại dữ liệu sau khi thao tác thành công
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
