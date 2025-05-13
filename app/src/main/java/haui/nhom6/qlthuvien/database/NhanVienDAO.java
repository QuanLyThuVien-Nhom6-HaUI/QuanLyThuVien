package haui.nhom6.qlthuvien.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import haui.nhom6.qlthuvien.model.NhanVien;

import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public NhanVienDAO(Context context) {
        // Luôn lấy application context để tránh null khi activity bị destroy
        Context appCtx = context.getApplicationContext();
        Log.d("NhanVienDAO", "Init with ctx=" + appCtx);
        dbHelper = new DatabaseHelper(appCtx);
        Log.d("NhanVienDAO", "dbHelper=" + dbHelper);
    }

    //1.lay ra danh sách nhan vien
    @SuppressLint("Range")
    public List<NhanVien> getAllNhanVien() {
        if (dbHelper == null) {
            throw new IllegalStateException("DatabaseHelper chưa được khởi tạo!");
        }
        db = dbHelper.getReadableDatabase();
        List<NhanVien> nhanVienList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM NhanVien", null);

        if (cursor.moveToFirst()) {
            do {
                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(cursor.getString(cursor.getColumnIndex("maNhanVien")));
                nv.setTenNhanVien(cursor.getString(cursor.getColumnIndex("tenNhanVien")));
                nv.setNgaySinh(cursor.getString(cursor.getColumnIndex("ngaySinh")));
                nv.setQueQuan(cursor.getString(cursor.getColumnIndex("queQuan")));
                nv.setSoDienThoai(cursor.getString(cursor.getColumnIndex("soDienThoai")));
                nhanVienList.add(nv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return nhanVienList;
    }
    //2. Them nhan vien moi
    public long themNhanVien(NhanVien nhanVien) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maNhanVien", nhanVien.getMaNhanVien());
        values.put("tenNhanVien", nhanVien.getTenNhanVien());
        values.put("ngaySinh",    nhanVien.getNgaySinh());
        values.put("queQuan",     nhanVien.getQueQuan());
        values.put("soDienThoai", nhanVien.getSoDienThoai());
        long result = db.insert("NhanVien", null, values);
        Log.d("DB_INSERT", "Đã thêm nhân viên: " + nhanVien.getTenNhanVien() + " - ID: " + result);
        db.close();
        return result;
    }
    //3. Cap nhat nhan vien
    public long updateNhanVien(NhanVien nhanVien) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenNhanVien",    nhanVien.getTenNhanVien());
        values.put("ngaySinh",       nhanVien.getNgaySinh());
        values.put("queQuan",        nhanVien.getQueQuan());
        values.put("soDienThoai",    nhanVien.getSoDienThoai());
        long rows = db.update("NhanVien", values, "maNhanVien = ?", new String[]{nhanVien.getMaNhanVien()});
        db.close();
        return rows;
    }
    //4.Xoa nhan vien
    public int deleteNhanVien(String maNhanVien) {
        db = dbHelper.getWritableDatabase();
        int rows = db.delete("NhanVien", "maNhanVien = ?", new String[]{maNhanVien});
        db.close();
        return rows;
    }
}
