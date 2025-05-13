package haui.nhom6.qlthuvien.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import haui.nhom6.qlthuvien.model.NhanVien;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QLThuVien.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private final String dbPath;

    private static final String TAIKHOAN = "TaiKhoan";
    private static final String COT_MA_TAIKHOAN = "maTaiKhoan";
    private static final String COT_TEN_DANGNHAP = "tenDangNhap";
    private static final String COT_MATKHAU = "matKhau";
    private static final String COT_VAITRO = "vaiTro";
    private static final String COT_MA_NHANVIEN = "maNhanVien";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.dbPath = context.getDatabasePath(DATABASE_NAME).getPath();
        copyDatabaseIfNeeded(); // Copy DB khi khởi tạo
    }

    private void copyDatabaseIfNeeded() {
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            dbFile.getParentFile().mkdirs();
            try (InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                 OutputStream outputStream = new FileOutputStream(dbPath)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                System.out.println("Database copied!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Database already exists.");
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp nếu cần
    }


    public boolean kiemTraTenDangNhap(String tenDangNhap) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cot = {COT_TEN_DANGNHAP};
        String dieuKien = COT_TEN_DANGNHAP + "=?";
        Cursor cursor = db.query(TAIKHOAN, cot, dieuKien, new String[]{tenDangNhap},
                null, null, null);
        boolean tonTai = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return tonTai;
    }

    public boolean kiemTraMatKhau(String tenDangNhap, String matKhau) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cot = {COT_MATKHAU};
        String dieuKien = COT_TEN_DANGNHAP + "=? AND " + COT_MATKHAU + "=?";
        Cursor cursor = db.query(TAIKHOAN, cot, dieuKien, new String[]{tenDangNhap, matKhau}, null,
                null, null);
        boolean dungMatKhau = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return dungMatKhau;
    }

    public String layVaiTro(String tenDangNhap) {
        SQLiteDatabase db = this.getReadableDatabase();
        String vaiTro = null;
        Cursor cursor = db.query(TAIKHOAN, new String[]{COT_VAITRO}, COT_TEN_DANGNHAP + "=?",
                new String[]{tenDangNhap}, null, null, null);
        if (cursor.moveToFirst()) {
            vaiTro = cursor.getString(cursor.getColumnIndexOrThrow(COT_VAITRO));
        }
        cursor.close();
        db.close();
        return vaiTro;
    }

}
