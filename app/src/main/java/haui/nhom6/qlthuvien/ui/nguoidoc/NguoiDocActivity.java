package haui.nhom6.qlthuvien.ui.nguoidoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import haui.nhom6.qlthuvien.AdminActivity;
import haui.nhom6.qlthuvien.MainActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.UserActivity;
import haui.nhom6.qlthuvien.adapter.NguoiDocAdapter;
import haui.nhom6.qlthuvien.model.NguoiDoc;

public class NguoiDocActivity extends AppCompatActivity implements NguoiDocContract.View {

    private ListView listViewNguoiDoc;
    private Button btnThemNguoiDoc, btnPrevious, btnNext;
    private EditText edtSearch;
    private TextView tvPageInfo;
    private ImageView icArrowBack, icUser;
    private NguoiDocPresenter presenter;
    private NguoiDocAdapter adapter;
    private int currentPage = 1;
    private boolean isSearching = false;

    private static final int ACTIVE_BACKGROUND_COLOR = 0xFF000000;
    private static final int ACTIVE_TEXT_COLOR = 0xFFFDFDFD;
    private static final int INACTIVE_BACKGROUND_COLOR = 0xFFB0B0B0;
    private static final int INACTIVE_TEXT_COLOR = 0xFF333333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_nguoidoc);

            // Ánh xạ view
            listViewNguoiDoc = findViewById(R.id.listViewNguoiDoc);
            btnThemNguoiDoc = findViewById(R.id.btnThemNguoiDoc);
            btnPrevious = findViewById(R.id.btnPrevious);
            btnNext = findViewById(R.id.btnNext);
            tvPageInfo = findViewById(R.id.tvPageInfo);
            edtSearch = findViewById(R.id.edt_search);
            icArrowBack = findViewById(R.id.icArrowBack);
            icUser = findViewById(R.id.icUser);

            // Khởi tạo presenter
            presenter = new NguoiDocPresenter(this, this);

            // Gọi load danh sách ban đầu
            loadPage();

            // Xử lý sự kiện nhấn Thêm người đọc
            btnThemNguoiDoc.setOnClickListener(v -> {
                Intent intent = new Intent(this, NguoiDocAddActivity.class);
                startActivity(intent);
            });

            // Xử lý back navigation
            icArrowBack.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String role = prefs.getString("role", null);
                Intent intent;
                if (role == null || role.equals("nhanvien")) {
                    intent = new Intent(NguoiDocActivity.this, UserActivity.class);
                } else if (role.equals("quanly")) {
                    intent = new Intent(NguoiDocActivity.this, AdminActivity.class);
                } else {
                    intent = new Intent(NguoiDocActivity.this, MainActivity.class);
                }
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

            // Click 1 lần để xem chi tiết
            listViewNguoiDoc.setOnItemClickListener((parent, view, position, id) -> {
                NguoiDoc selectedNguoiDoc = (NguoiDoc) parent.getItemAtPosition(position);
                Intent intent = new Intent(NguoiDocActivity.this, NguoiDocDetailActivity.class);
                intent.putExtra("nguoidoc", selectedNguoiDoc);
                startActivity(intent);
            });

            // Long click để xóa với thông báo xác nhận
            listViewNguoiDoc.setOnItemLongClickListener((parent, view, position, id) -> {
                NguoiDoc selectedNguoiDoc = (NguoiDoc) parent.getItemAtPosition(position);
                new AlertDialog.Builder(NguoiDocActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa người đọc này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            presenter.deleteNguoiDoc(selectedNguoiDoc.getMaNguoiDoc());
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
            });

            // Chức năng tìm kiếm
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String keyword = s.toString().trim();
                    if (keyword.isEmpty()) {
                        isSearching = false;
                        currentPage = 1;
                        loadPage();
                        tvPageInfo.setVisibility(View.VISIBLE);
                        btnPrevious.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.VISIBLE);
                    } else {
                        isSearching = true;
                        presenter.timKiemNguoiDoc(keyword);
                        tvPageInfo.setVisibility(View.GONE);
                        btnPrevious.setVisibility(View.GONE);
                        btnNext.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Xử lý chuyển trang
            btnPrevious.setOnClickListener(v -> {
                if (currentPage > 1) {
                    currentPage--;
                    loadPage();
                }
            });

            btnNext.setOnClickListener(v -> {
                if (currentPage < presenter.getTotalPages()) {
                    currentPage++;
                    loadPage();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void loadPage() {
        if (!isSearching) {
            try {
                int totalPages = presenter.getTotalPages();
                if (totalPages == 0) {
                    currentPage = 1;
                    showNguoiDocList(null);
                    updatePageInfo(currentPage, totalPages);
                    return;
                }
                if (currentPage > totalPages) {
                    currentPage = totalPages;
                }
                presenter.loadNguoiDocList(currentPage);
            } catch (Exception e) {
                showError("Lỗi tải danh sách: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPage();
    }

    @Override
    public void showNguoiDocList(List<NguoiDoc> list) {
        adapter = new NguoiDocAdapter(this, list, currentPage);
        listViewNguoiDoc.setAdapter(adapter);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        loadPage();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updatePageInfo(int currentPage, int totalPages) {
        this.currentPage = currentPage;
        tvPageInfo.setText(String.format("Trang %d/%d", currentPage, totalPages));

        if (currentPage == 1) {
            btnPrevious.setBackgroundTintList(ColorStateList.valueOf(INACTIVE_BACKGROUND_COLOR));
            btnPrevious.setTextColor(INACTIVE_TEXT_COLOR);
        } else {
            btnPrevious.setBackgroundTintList(ColorStateList.valueOf(ACTIVE_BACKGROUND_COLOR));
            btnPrevious.setTextColor(ACTIVE_TEXT_COLOR);
        }

        if (currentPage == totalPages || totalPages == 0) {
            btnNext.setBackgroundTintList(ColorStateList.valueOf(INACTIVE_BACKGROUND_COLOR));
            btnNext.setTextColor(INACTIVE_TEXT_COLOR);
        } else {
            btnNext.setBackgroundTintList(ColorStateList.valueOf(ACTIVE_BACKGROUND_COLOR));
            btnNext.setTextColor(ACTIVE_TEXT_COLOR);
        }

        btnPrevious.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
    }
}