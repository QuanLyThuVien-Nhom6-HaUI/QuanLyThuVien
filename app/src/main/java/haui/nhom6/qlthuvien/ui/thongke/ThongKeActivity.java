package haui.nhom6.qlthuvien.ui.thongke;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.TableLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import haui.nhom6.qlthuvien.AdminActivity;
import haui.nhom6.qlthuvien.MainActivity;
import haui.nhom6.qlthuvien.R;
import haui.nhom6.qlthuvien.database.DatabaseHelper;
import haui.nhom6.qlthuvien.database.ThongKeDAO;

public class ThongKeActivity extends AppCompatActivity {

    private ThongKeDAO thongKeDAO;
    private TextView tvTotalBook, tvBookBorrowed, tvTotalUser;
    private TableLayout tableTopBooks;
    private ImageView icArrowBack;
    private ImageView icUser;
    private LineChart lineChart; // Thay BarChart bằng LineChart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thongke);

        // Khởi tạo Database và DAO
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        thongKeDAO = new ThongKeDAO(dbHelper);

        // Ánh xạ các view
        tvTotalBook = findViewById(R.id.tvTotalBook);
        tvBookBorrowed = findViewById(R.id.tvBookBorrowed);
        tvTotalUser = findViewById(R.id.tvTotalUser);
        tableTopBooks = findViewById(R.id.tableTopBooks); // Đảm bảo ID này có trong XML
        lineChart = findViewById(R.id.lineChart); // Ánh xạ LineChart
        icArrowBack = findViewById(R.id.icArrowBack);
        icUser = findViewById(R.id.icUser);

        // Xử lý logout khi nhấn ic_user
        icUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        icArrowBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            finish();
        });

        // Áp dụng padding cho window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load dữ liệu thống kê
        loadStatistics();
    }

    private void loadStatistics() {
        // 1. Tổng số đầu sách
        int totalTitleBooks = thongKeDAO.getTotalTitleBooks();
        tvTotalBook.setText(String.valueOf(totalTitleBooks));

        // 2. Tổng số sách
        int totalBooks = thongKeDAO.getTotalBookQuantity();
        // 3. Số sách đang cho mượn
        int borrowedBooks = thongKeDAO.getBorrowedBookCount();
        tvBookBorrowed.setText(String.valueOf(borrowedBooks) + "/" + String.valueOf(totalBooks));

        // 4. Tổng số độc giả
        int totalReaders = thongKeDAO.getTotalReaders();
        tvTotalUser.setText(String.valueOf(totalReaders));

        // 5. Biểu đồ số lượt mượn theo tháng
        Map<String, Integer> monthlyStats = thongKeDAO.getBorrowingStatsByMonth();
        setupLineChart(monthlyStats); // Sử dụng LineChart thay vì BarChart

        // 6. Top 5 sách mượn nhiều nhất
        updateTopBooksTable(thongKeDAO.getTop5MostBorrowedBooks());
    }

    private void setupLineChart(Map<String, Integer> monthlyStats) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;

        // Chuyển dữ liệu từ Map sang Entry
        for (Map.Entry<String, Integer> entry : monthlyStats.entrySet()) {
            entries.add(new Entry(index++, entry.getValue())); // Sử dụng index làm trục X, value làm trục Y
            labels.add(entry.getKey()); // Lưu nhãn tháng
        }

        if (entries.isEmpty()) {
            lineChart.setNoDataText("Không có dữ liệu để hiển thị");
            return;
        }

        // Tạo LineDataSet
        LineDataSet lineDataSet = new LineDataSet(entries, "Số lượt mượn");
        lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setDrawValues(true); // Hiển thị giá trị trên biểu đồ
        lineDataSet.setLineWidth(2f); // Độ dày đường

        // Tạo LineData
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        // Cấu hình LineChart
        lineChart.getDescription().setText("Số lượt mượn theo tháng");
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels)); // Đặt nhãn trục X
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt nhãn dưới
        lineChart.getXAxis().setGranularity(1f); // Đảm bảo mỗi nhãn cách đều
        lineChart.getAxisLeft().setAxisMinimum(0f); // Bắt đầu trục Y từ 0
        lineChart.getAxisRight().setEnabled(false); // Tắt trục Y bên phải
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            lineChart.animateY(1000); // Hiệu ứng animation
        }
        lineChart.invalidate(); // Làm mới biểu đồ
    }

    private void updateTopBooksTable(List<Map<String, Object>> topBooks) {
        TableLayout tableLayout = findViewById(R.id.tableTopBooks);
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1); // Xóa các hàng cũ

        int count = 1;
        for (Map<String, Object> book : topBooks) {
            TableRow row = new TableRow(this);
            TextView tvBookName = new TextView(this);
            TextView tvBookQuantity = new TextView(this);

            tvBookName.setText((String) book.get("tenSach"));
            tvBookQuantity.setText(String.valueOf(book.get("borrowCount")));
            tvBookName.setPadding(8, 8, 8, 8);
            tvBookQuantity.setPadding(8, 8, 8, 8);
            tvBookName.setTextColor(getResources().getColor(android.R.color.black));
            tvBookQuantity.setTextColor(getResources().getColor(android.R.color.black));

            row.addView(tvBookName, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            row.addView(tvBookQuantity, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableLayout.addView(row);

            count++;
            if (count > 5) break; // Giới hạn top 5
        }
    }
}