package haui.nhom6.qlthuvien.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import haui.nhom6.qlthuvien.model.NguoiDoc;

import java.util.ArrayList;
import java.util.List;

public class NguoiDocDAO {
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public NguoiDocDAO(Context context) {
        Context appCtx = context.getApplicationContext();
        Log.d("NguoiDocDAO", "Init with ctx=" + appCtx);
        dbHelper = new DatabaseHelper(appCtx);
        Log.d("NguoiDocDAO", "dbHelper=" + dbHelper);
    }

    // 1. Lấy ra danh sách người đọc
    @SuppressLint("Range")
    public List<NguoiDoc> getAllNguoiDoc() {
        if (dbHelper == null) {
            throw new IllegalStateException("DatabaseHelper chưa được khởi tạo!");
        }
        db = dbHelper.getReadableDatabase();
        List<NguoiDoc> nguoiDocList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM NguoiDoc", null);

        if (cursor.moveToFirst()) {
            do {
                NguoiDoc nd = new NguoiDoc();
                nd.setMaNguoiDoc(cursor.getString(cursor.getColumnIndex("maNguoiDoc")));
                nd.setTenNguoiDoc(cursor.getString(cursor.getColumnIndex("tenNguoiDoc")));
                nd.setCccd(cursor.getString(cursor.getColumnIndex("cccd")));
                nd.setSoDienThoai(cursor.getString(cursor.getColumnIndex("soDienThoai")));
                nd.setGioiTinh(cursor.getString(cursor.getColumnIndex("gioiTinh")));
                nd.setDiaChi(cursor.getString(cursor.getColumnIndex("diaChi")));
                nguoiDocList.add(nd);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return nguoiDocList;
    }

    // 2. Thêm người đọc mới
    public long themNguoiDoc(NguoiDoc nguoiDoc) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maNguoiDoc", nguoiDoc.getMaNguoiDoc());
        values.put("tenNguoiDoc", nguoiDoc.getTenNguoiDoc());
        values.put("cccd", nguoiDoc.getCccd());
        values.put("soDienThoai", nguoiDoc.getSoDienThoai());
        values.put("gioiTinh", nguoiDoc.getGioiTinh());
        values.put("diaChi", nguoiDoc.getDiaChi());
        long result = db.insert("NguoiDoc", null, values);
        Log.d("DB_INSERT", "Đã thêm người đọc: " + nguoiDoc.getTenNguoiDoc() + " - ID: " + result);
        db.close();
        return result;
    }

    // 3. Cập nhật người đọc
    public long updateNguoiDoc(NguoiDoc nguoiDoc) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenNguoiDoc", nguoiDoc.getTenNguoiDoc());
        values.put("cccd", nguoiDoc.getCccd());
        values.put("soDienThoai", nguoiDoc.getSoDienThoai());
        values.put("gioiTinh", nguoiDoc.getGioiTinh());
        values.put("diaChi", nguoiDoc.getDiaChi());
        long rows = db.update("NguoiDoc", values, "maNguoiDoc = ?", new String[]{nguoiDoc.getMaNguoiDoc()});
        db.close();
        return rows;
    }

    // 4. Xóa người đọc
    public int deleteNguoiDoc(String maNguoiDoc) {
        db = dbHelper.getWritableDatabase();
        int rows = db.delete("NguoiDoc", "maNguoiDoc = ?", new String[]{maNguoiDoc});
        db.close();
        return rows;
    }
}