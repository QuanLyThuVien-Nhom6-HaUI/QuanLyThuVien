package haui.nhom6.qlthuvien.ui.sach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.adapter.SachAdapter;
import haui.nhom6.qlthuvien.model.Sach;
import java.util.List;

public class SachActivity extends AppCompatActivity implements SachContract.View {

    private ListView listViewSach;
    private Button btnThemSach, btnPrevious, btnNext;
    private TextView tvPageInfo;
    private SachPresenter presenter;
    private SachAdapter adapter;
    private int currentPage = 1;

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

        // Khởi tạo presenter
        presenter = new SachPresenter(this, this);

        // Gọi load danh sách ban đầu
        loadPage();

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
                    })
                    .setNegativeButton("Hủy", null)
                    .show();

            return true; // Báo là đã xử lý long click
        });
    }

    private void loadPage() {
        presenter.loadSachList(currentPage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPage(); // Reload danh sách mỗi khi quay lại
    }

    @Override
    public void showSachList(List<Sach> list) {
        adapter = new SachAdapter(this, list, currentPage);
        listViewSach.setAdapter(adapter);
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
        tvPageInfo.setText(String.format("Trang %d/%d", currentPage, totalPages));
        btnPrevious.setEnabled(currentPage > 1); // Cập nhật trạng thái cho btnPrevious
        btnNext.setEnabled(currentPage < totalPages); // Cập nhật trạng thái cho btnNext
    }
}