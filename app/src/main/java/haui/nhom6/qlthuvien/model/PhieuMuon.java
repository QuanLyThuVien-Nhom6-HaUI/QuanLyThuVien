package haui.nhom6.qlthuvien.model;

import java.io.Serializable;
import java.util.Date;

public class PhieuMuon implements Serializable {
    private String maPhieu;
    private String maNhanVien;
    private String maNguoiDoc;
    private Date ngayMuon;
    private Date hanTraSach;
    private String tenNhanVien; // Thêm nếu cần
    private String tenNguoiDoc; // Thêm nếu cần

    // Constructor
    public PhieuMuon() {}

    public PhieuMuon(String maPhieu, String maNhanVien, String maNguoiDoc, Date ngayMuon, Date hanTraSach) {
        this.maPhieu = maPhieu;
        this.maNhanVien = maNhanVien;
        this.maNguoiDoc = maNguoiDoc;
        this.ngayMuon = ngayMuon;
        this.hanTraSach = hanTraSach;
    }

    // Getters và Setters
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getMaNguoiDoc() { return maNguoiDoc; }
    public void setMaNguoiDoc(String maNguoiDoc) { this.maNguoiDoc = maNguoiDoc; }

    public Date getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(Date ngayMuon) { this.ngayMuon = ngayMuon; }

    public Date getHanTraSach() { return hanTraSach; }
    public void setHanTraSach(Date hanTraSach) { this.hanTraSach = hanTraSach; }

    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }

    public String getTenNguoiDoc() { return tenNguoiDoc; }
    public void setTenNguoiDoc(String tenNguoiDoc) { this.tenNguoiDoc = tenNguoiDoc; }
}