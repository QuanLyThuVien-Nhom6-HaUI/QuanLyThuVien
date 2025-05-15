package haui.nhom6.qlthuvien.ui.sach;

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
import haui.nhom6.qlthuvien.adapter.SachAdapter;
import haui.nhom6.qlthuvien.model.Sach;

public class SachActivity extends AppCompatActivity implements SachContract.View {

    private ListView listViewSach;
    private Button btnThemSach, btnPrevious, btnNext;
    private EditText edtSearch;
    private TextView tvPageInfo;
    private SachPresenter presenter;
    private SachAdapter adapter;
    private ImageView icArrowBack, icUser;
    private int currentPage = 1;
    private boolean isSearching = false; // Biến lưu trạng thái có đang search hay không

    // Định nghĩa các hằng số màu
    private static final int ACTIVE_BACKGROUND_COLOR = 0xFF000000; // Đen
    private static final int ACTIVE_TEXT_COLOR = 0xFFFDFDFD; // Trắng nhạt
    private static final int INACTIVE_BACKGROUND_COLOR = 0xFFB0B0B0; // Xám nhạt
    private static final int INACTIVE_TEXT_COLOR = 0xFF333333; // Xám đậm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sach);

        // Ánh xạ view
        listViewSach = findViewById(R.id.listViewSach);
        btnThemSach = findViewById(R.id.btnThemSach);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        tvPageInfo = findViewById(R.id.tvPageInfo);
        icArrowBack = findViewById(R.id.icArrowBack);
        icUser = findViewById(R.id.icUser);
        edtSearch = findViewById(R.id.edtSearch);

        // Khởi tạo presenter
        presenter = new SachPresenter(this, this);

        // Gọi load danh sách ban đầu
        loadPage();


        // Sự kiện nút quay lại
        icArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy vai trò từ SharedPreferences
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String role = prefs.getString("role", null);
                if (role == null) {
                    Intent intent = new Intent(SachActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

                Intent intent;
                if (role.equals("quanly")) {
                    intent = new Intent(SachActivity.this, AdminActivity.class);
                } else {
                    intent = new Intent(SachActivity.this, UserActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });

        // Xử lý logout khi nhấn ic_user
        icUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        //Chức năng tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                if (keyword.isEmpty()) {
                    isSearching = false;
                    currentPage = 1; // trở về trang 1 sau khi không tìm kiếm nữa
                    loadPage();
                    // Hiện lại phần phân trang
                    tvPageInfo.setVisibility(View.VISIBLE);
                    btnPrevious.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                } else {
                    isSearching = true;
                    presenter.timKiemSach(keyword);
                    // Ẩn phần phân trang khi đang search
                    tvPageInfo.setVisibility(View.GONE);
                    btnPrevious.setVisibility(View.GONE);
                    btnNext.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý sự kiện nhấn Thêm sách
        btnThemSach.setOnClickListener(v -> {
            Intent intent = new Intent(this, SachAddActivity.class);
            startActivity(intent);
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

        // Click 1 lần để xem chi tiết
        listViewSach.setOnItemClickListener((parent, view, position, id) -> {
            Sach selectedSach = (Sach) parent.getItemAtPosition(position);
            Intent intent = new Intent(SachActivity.this, SachDetailActivity.class);
            intent.putExtra("sach", selectedSach);
            startActivity(intent);
        });

        // Nhấn và giữ để xóa
        listViewSach.setOnItemLongClickListener((parent, view, position, id) -> {
            Sach selectedSach = (Sach) parent.getItemAtPosition(position);

            new AlertDialog.Builder(SachActivity.this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa sách này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        presenter.deleteSach(selectedSach.getMaSach());
                        loadPage();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();

            return true; // Báo là đã xử lý long click
        });
    }

    private void loadPage() {
        if (!isSearching) {
            // Kiểm tra tính hợp lệ của currentPage
            int totalPages = presenter.getTotalPages();
            if (totalPages == 0) {
                currentPage = currentPage-1; // Đặt về trang 1 nếu không có sách
                showSachList(null); // Hiển thị danh sách trống
                updatePageInfo(currentPage, totalPages);
                return;
            }
            if (currentPage > totalPages) {
                currentPage = totalPages; // Điều chỉnh về trang cuối nếu vượt quá
            }
            presenter.loadSachList(currentPage);
        }
    }

    @Override
    public void showSachList(List<Sach> list) {
        adapter = new SachAdapter(this, list, currentPage);
        listViewSach.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPage(); // Reload danh sách mỗi khi quay lại
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        loadPage(); // Cập nhật lại danh sách sau khi xóa
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updatePageInfo(int currentPage, int totalPages) {
        this.currentPage = currentPage;
        tvPageInfo.setText(String.format("Trang %d/%d", currentPage, totalPages));

        // Cập nhật màu sắc cho btnPrevious
        if (currentPage == 1) {
            btnPrevious.setBackgroundTintList(ColorStateList.valueOf(INACTIVE_BACKGROUND_COLOR));
            btnPrevious.setTextColor(INACTIVE_TEXT_COLOR);
        } else {
            btnPrevious.setBackgroundTintList(ColorStateList.valueOf(ACTIVE_BACKGROUND_COLOR));
            btnPrevious.setTextColor(ACTIVE_TEXT_COLOR);
        }

        // Cập nhật màu sắc cho btnNext
        if (currentPage == totalPages || totalPages == 0) {
            btnNext.setBackgroundTintList(ColorStateList.valueOf(INACTIVE_BACKGROUND_COLOR));
            btnNext.setTextColor(INACTIVE_TEXT_COLOR);
        } else {
            btnNext.setBackgroundTintList(ColorStateList.valueOf(ACTIVE_BACKGROUND_COLOR));
            btnNext.setTextColor(ACTIVE_TEXT_COLOR);
        }

        // Cập nhật trạng thái enabled
        btnPrevious.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
    }
}