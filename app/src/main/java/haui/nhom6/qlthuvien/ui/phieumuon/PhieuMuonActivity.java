package haui.nhom6.qlthuvien.ui.phieumuon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import haui.nhom6.qlthuvien.AdminActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.UserActivity;
import haui.nhom6.qlthuvien.adapter.PhieuMuonAdapter;
import haui.nhom6.qlthuvien.database.PhieuMuonDAO;
import haui.nhom6.qlthuvien.model.PhieuMuon;

import java.util.ArrayList;
import java.util.List;

public class PhieuMuonActivity extends AppCompatActivity {
    private RecyclerView rvPhieuMuon;
    private EditText etSearch;
    private PhieuMuonDAO phieuMuonDAO;
    private PhieuMuonAdapter phieuMuonAdapter;
    private List<PhieuMuon> phieuMuonList; // Danh sách đầy đủ
    private List<PhieuMuon> filteredList; // Danh sách sau khi lọc (tìm kiếm)
    private List<PhieuMuon> currentPageList; // Danh sách hiển thị trên trang hiện tại
    private Button btnPreviousPage, btnNextPage;
    private TextView tvPageInfo;
    private int currentPage = 1; // Trang hiện tại
    private final int ITEMS_PER_PAGE = 5; // Số lượng item mỗi trang
    private int totalPages; // Tổng số trang

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_muon);

        // Khởi tạo các view
        rvPhieuMuon = findViewById(R.id.rvPhieuMuon);
        etSearch = findViewById(R.id.etSearch);
        btnPreviousPage = findViewById(R.id.btnPreviousPage);
        btnNextPage = findViewById(R.id.btnNextPage);
        tvPageInfo = findViewById(R.id.tvPageInfo);
        phieuMuonDAO = new PhieuMuonDAO(this);

        // Load danh sách phiếu mượn
        loadPhieuMuonList();

        // Sự kiện tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterPhieuMuon(s.toString());
            }
        });

        // Sự kiện nút thêm phiếu mượn
        findViewById(R.id.btnThemPhieuMuon).setOnClickListener(v -> {
            startActivity(new Intent(PhieuMuonActivity.this, ThemPhieuMuonActivity.class));
        });

        // Sự kiện nút quay lại
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String role = prefs.getString("role", "nhanvien");
            Intent intent = role.equals("quanly") ?
                    new Intent(PhieuMuonActivity.this, AdminActivity.class) :
                    new Intent(PhieuMuonActivity.this, UserActivity.class);
            startActivity(intent);
            finish();
        });

        // Sự kiện nút phân trang
        btnPreviousPage.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                updatePage();
            }
        });

        btnNextPage.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                updatePage();
            }
        });
    }

    private void loadPhieuMuonList() {
        phieuMuonList = phieuMuonDAO.getAllPhieuMuon();
        filteredList = new ArrayList<>(phieuMuonList); // Khởi tạo danh sách lọc ban đầu
        calculateTotalPages();
        currentPage = 1; // Đặt lại về trang đầu tiên
        updatePage();
    }

    private void filterPhieuMuon(String keyword) {
        filteredList.clear();
        if (keyword.isEmpty()) {
            filteredList.addAll(phieuMuonList);
        } else {
            String searchText = keyword.toLowerCase().trim();
            for (PhieuMuon pm : phieuMuonList) {
                if ((pm.getTenNhanVien() != null && pm.getTenNhanVien().toLowerCase().contains(searchText)) ||
                        (pm.getTenNguoiDoc() != null && pm.getTenNguoiDoc().toLowerCase().contains(searchText))) {
                    filteredList.add(pm);
                }
            }
        }
        calculateTotalPages();
        currentPage = 1; // Đặt lại về trang đầu tiên sau khi lọc
        updatePage();
    }

    private void calculateTotalPages() {
        totalPages = (int) Math.ceil((double) filteredList.size() / ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1; // Đảm bảo ít nhất 1 trang
    }

    private void updatePage() {
        // Tính chỉ số bắt đầu và kết thúc của danh sách trang hiện tại
        int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, filteredList.size());

        // Lấy danh sách con cho trang hiện tại
        currentPageList = new ArrayList<>();
        if (!filteredList.isEmpty()) {
            currentPageList = filteredList.subList(startIndex, endIndex);
        }

        // Cập nhật RecyclerView
        phieuMuonAdapter = new PhieuMuonAdapter(currentPageList);
        rvPhieuMuon.setLayoutManager(new LinearLayoutManager(this));
        rvPhieuMuon.setAdapter(phieuMuonAdapter);

        // Cập nhật thông tin trang
        tvPageInfo.setText("Trang " + currentPage + "/" + totalPages);

        // Ẩn/hiện nút phân trang
        btnPreviousPage.setEnabled(currentPage > 1);
        btnNextPage.setEnabled(currentPage < totalPages);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật danh sách khi quay lại activity
        loadPhieuMuonList();
        etSearch.setText(""); // Đặt lại ô tìm kiếm
    }
}