package haui.nhom6.qlthuvien.ui.nguoidoc;

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
import haui.nhom6.qlthuvien.adapter.NguoiDocAdapter;
import haui.nhom6.qlthuvien.model.NguoiDoc;
import haui.nhom6.qlthuvien.ui.nguoidoc.NguoiDocContract;
import haui.nhom6.qlthuvien.ui.nguoidoc.NguoiDocPresenter;

public class NguoiDocActivity extends AppCompatActivity implements NguoiDocContract.View {

    private ListView listViewNguoiDoc;
    private Button btnThemNguoiDoc;
    private EditText edtSearch;
    private ImageView icArrowBack;
    private ImageView icUser;
    private NguoiDocPresenter presenter;
    private NguoiDocAdapter adapter;
    private List<NguoiDoc> fullList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoidoc);

        // Ánh xạ view
        listViewNguoiDoc = findViewById(R.id.listViewNguoiDoc);
        btnThemNguoiDoc = findViewById(R.id.btnThemNguoiDoc);
        edtSearch = findViewById(R.id.edt_search);
        icArrowBack = findViewById(R.id.icArrowBack);
        icUser = findViewById(R.id.icUser);

        // Khởi tạo presenter
        presenter = new NguoiDocPresenter(this, this);

        // Gọi load danh sách ban đầu
        presenter.loadNguoiDocList();

        // Xử lý sự kiện nhấn Thêm người đọc
        btnThemNguoiDoc.setOnClickListener(v -> {
            Intent intent = new Intent(this, NguoiDocAddActivity.class);
            startActivity(intent);
        });

        // Xử lý back navigation
        icArrowBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            finish();
        });
        icUser = findViewById(R.id.icUser);
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
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá người đọc này?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        presenter.deleteNguoiDoc(selectedNguoiDoc.getMaNguoiDoc());
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();

            return true; // Báo là đã xử lý long click
        });

        // Thêm chức năng tìm kiếm theo tên
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
        // Reload danh sách mỗi khi quay lại từ màn hình khác
        presenter.loadNguoiDocList();
    }

    @Override
    public void showNguoiDocList(List<NguoiDoc> list) {
        fullList = new ArrayList<>(list); // Lưu danh sách đầy đủ để lọc
        adapter = new NguoiDocAdapter(this, list);
        listViewNguoiDoc.setAdapter(adapter);
    }

    private void filterList(String query) {
        if (fullList != null) {
            List<NguoiDoc> filteredList = new ArrayList<>();
            for (NguoiDoc nguoiDoc : fullList) {
                if (nguoiDoc.getTenNguoiDoc().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(nguoiDoc);
                }
            }
            adapter = new NguoiDocAdapter(this, filteredList);
            listViewNguoiDoc.setAdapter(adapter);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        presenter.loadNguoiDocList(); // Cập nhật lại danh sách sau khi xoá
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}