package haui.nhom6.qlthuvien.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ThongKeDAO(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.db = dbHelper.getReadableDatabase();
    }

    // 1. Tổng số sách hiện có
    public int getTotalBookQuantity() {
        String query = "SELECT SUM(soLuong) FROM Sach";
        Cursor cursor = db.rawQuery(query, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }

//    2. Số đầu sách
    public int getTotalTitleBooks() {
        String query = "SELECT COUNT(DISTINCT tenSach) FROM Sach";
        Cursor cursor = db.rawQuery(query, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }


    // 3. Số sách đang cho mượn
    public int getBorrowedBookCount() {
        String query = "SELECT SUM(soLuong) FROM ChiTietPhieu";
        Cursor cursor = db.rawQuery(query, null);
        int borrowedCount = 0;
        if (cursor.moveToFirst()) {
            borrowedCount = cursor.getInt(0);
        }
        cursor.close();
        return borrowedCount;
    }

    // 4. Tổng số độc giả đã đăng ký
    public int getTotalReaders() {
        String query = "SELECT COUNT(*) FROM NguoiDoc";
        Cursor cursor = db.rawQuery(query, null);
        int totalReaders = 0;
        if (cursor.moveToFirst()) {
            totalReaders = cursor.getInt(0);
        }
        cursor.close();
        return totalReaders;
    }

    // 5. Tổng số lượt mượn sách theo tháng (dữ liệu dạng Map để vẽ biểu đồ)
    public Map<String, Integer> getBorrowingStatsByMonth() {
        Map<String, Integer> stats = new HashMap<>();
        String query = "SELECT strftime('%Y-%m', ngayMuon) AS month, COUNT(*) AS count " +
                "FROM PhieuMuonTra " +
                "GROUP BY strftime('%Y-%m', ngayMuon)";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String month = cursor.getString(0); // Định dạng YYYY-MM
            int count = cursor.getInt(1);
            stats.put(month, count);
        }
        cursor.close();
        return stats;
    }

    // 6. Top 5 sách được mượn nhiều nhất (dạng danh sách)
    public List<Map<String, Object>> getTop5MostBorrowedBooks() {
        List<Map<String, Object>> topBooks = new ArrayList<>();
        String query = "SELECT s.maSach, s.tenSach, COUNT(ctp.maSach) AS borrowCount " +
                "FROM Sach s " +
                "JOIN ChiTietPhieu ctp ON s.maSach = ctp.maSach " +
                "GROUP BY s.maSach, s.tenSach " +
                "ORDER BY borrowCount DESC " +
                "LIMIT 5";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Map<String, Object> book = new HashMap<>();
            book.put("maSach", cursor.getString(0));
            book.put("tenSach", cursor.getString(1));
            book.put("borrowCount", cursor.getInt(2));
            topBooks.add(book);
        }
        cursor.close();
        return topBooks;
    }
}