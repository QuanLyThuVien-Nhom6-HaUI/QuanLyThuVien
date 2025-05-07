package haui.nhom6.qlthuvien.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import haui.nhom6.qlthuvien.model.NguoiDoc;
import java.util.ArrayList;
import java.util.List;


public class NguoiDocDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public NguoiDocDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @SuppressLint("Range")
    public List<NguoiDoc> getAllNguoiDoc() {
        List<NguoiDoc> nguoiDocList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM NguoiDoc";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                NguoiDoc nguoiDoc = new NguoiDoc();
                nguoiDoc.setMaNguoiDoc(cursor.getString(cursor.getColumnIndex("maNguoiDoc")));
                nguoiDoc.setTenNguoiDoc(cursor.getString(cursor.getColumnIndex("tenNguoiDoc")));
                nguoiDoc.setCccd(cursor.getString(cursor.getColumnIndex("cccd")));
                nguoiDoc.setSoDienThoai(cursor.getString(cursor.getColumnIndex("soDienThoai")));
                nguoiDoc.setGioiTinh(cursor.getString(cursor.getColumnIndex("gioiTinh")));
                nguoiDoc.setDiaChi(cursor.getString(cursor.getColumnIndex("diaChi")));
                nguoiDocList.add(nguoiDoc);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return nguoiDocList;
    }
}