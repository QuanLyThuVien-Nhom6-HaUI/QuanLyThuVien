package haui.nhom6.qlthuvien.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import haui.nhom6.qlthuvien.model.PhieuMuon;
import java.util.ArrayList;
import java.util.List;



public class PhieuMuonDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public PhieuMuonDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @SuppressLint("Range")
    public List<PhieuMuon> getAllPhieuMuon() {
        List<PhieuMuon> phieuMuonList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM PhieuMuonTra";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PhieuMuon phieuMuon = new PhieuMuon();
                phieuMuon.setMaPhieu(cursor.getString(cursor.getColumnIndex("maPhieu")));
                phieuMuon.setMaNhanVien(cursor.getString(cursor.getColumnIndex("maNhanVien")));
                phieuMuon.setMaNguoiDoc(cursor.getString(cursor.getColumnIndex("maNguoiDoc")));
                phieuMuon.setNgayMuon(cursor.getString(cursor.getColumnIndex("ngayMuon")));
                phieuMuon.setHanTraSach(cursor.getString(cursor.getColumnIndex("hanTraSach")));
                phieuMuonList.add(phieuMuon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return phieuMuonList;
    }
}