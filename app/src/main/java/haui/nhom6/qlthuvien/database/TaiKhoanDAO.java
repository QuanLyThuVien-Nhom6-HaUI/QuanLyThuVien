package haui.nhom6.qlthuvien.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import haui.nhom6.qlthuvien.model.TaiKhoan;
import java.util.ArrayList;
import java.util.List;



public class TaiKhoanDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public TaiKhoanDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @SuppressLint("Range")
    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> taiKhoanList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM TaiKhoan";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TaiKhoan taiKhoan = new TaiKhoan();
                taiKhoan.setMaTaiKhoan(cursor.getString(cursor.getColumnIndex("maTaiKhoan")));
                taiKhoan.setTenDangNhap(cursor.getString(cursor.getColumnIndex("tenDangNhap")));
                taiKhoan.setMatKhau(cursor.getString(cursor.getColumnIndex("matKhau")));
                taiKhoan.setVaiTro(cursor.getString(cursor.getColumnIndex("vaiTro")));
                taiKhoan.setMaNhanVien(cursor.getString(cursor.getColumnIndex("maNhanVien")));
                taiKhoanList.add(taiKhoan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taiKhoanList;
    }
}