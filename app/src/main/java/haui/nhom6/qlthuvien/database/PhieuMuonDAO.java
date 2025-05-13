package haui.nhom6.qlthuvien.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import haui.nhom6.qlthuvien.model.PhieuMuon;

import java.text.SimpleDateFormat;
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
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT pm.*, nv.tenNhanVien, nd.tenNguoiDoc " +
                    "FROM PhieuMuonTra pm " +
                    "LEFT JOIN NhanVien nv ON pm.maNhanVien = nv.maNhanVien " +
                    "LEFT JOIN NguoiDoc nd ON pm.maNguoiDoc = nd.maNguoiDoc";
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    PhieuMuon phieuMuon = new PhieuMuon();
                    phieuMuon.setMaPhieu(cursor.getString(cursor.getColumnIndex("maPhieu")));
                    phieuMuon.setMaNhanVien(cursor.getString(cursor.getColumnIndex("maNhanVien")));
                    phieuMuon.setMaNguoiDoc(cursor.getString(cursor.getColumnIndex("maNguoiDoc")));
                    phieuMuon.setTenNhanVien(cursor.getString(cursor.getColumnIndex("tenNhanVien")));
                    phieuMuon.setTenNguoiDoc(cursor.getString(cursor.getColumnIndex("tenNguoiDoc")));
                    String ngayMuonStr = cursor.getString(cursor.getColumnIndex("ngayMuon"));
                    if (ngayMuonStr != null) {
                        try {
                            phieuMuon.setNgayMuon(new SimpleDateFormat("yyyy-MM-dd").parse(ngayMuonStr));
                        } catch (Exception e) {
                            phieuMuon.setNgayMuon(null);
                        }
                    }
                    String hanTraSachStr = cursor.getString(cursor.getColumnIndex("hanTraSach"));
                    if (hanTraSachStr != null) {
                        try {
                            phieuMuon.setHanTraSach(new SimpleDateFormat("yyyy-MM-dd").parse(hanTraSachStr));
                        } catch (Exception e) {
                            phieuMuon.setHanTraSach(null);
                        }
                    }
                    phieuMuonList.add(phieuMuon);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return phieuMuonList;
    }

    public long insertPhieuMuon(PhieuMuon phieuMuon) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maPhieu", phieuMuon.getMaPhieu());
        values.put("maNhanVien", phieuMuon.getMaNhanVien());
        values.put("maNguoiDoc", phieuMuon.getMaNguoiDoc());
        values.put("ngayMuon", new SimpleDateFormat("yyyy-MM-dd").format(phieuMuon.getNgayMuon()));
        values.put("hanTraSach", new SimpleDateFormat("yyyy-MM-dd").format(phieuMuon.getHanTraSach()));

        long result = db.insert("PhieuMuonTra", null, values);
        db.close();
        return result;
    }
}