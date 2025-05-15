package haui.nhom6.qlthuvien.model;

import java.io.Serializable;

public class NguoiDoc implements Serializable {
    private String maNguoiDoc;
    private String tenNguoiDoc;
    private String cccd;
    private String soDienThoai;
    private String gioiTinh;
    private String diaChi;

    public NguoiDoc() {}

    public NguoiDoc(String maNguoiDoc, String tenNguoiDoc, String cccd, String soDienThoai, String gioiTinh, String diaChi) {
        this.maNguoiDoc = maNguoiDoc;
        this.tenNguoiDoc = tenNguoiDoc;
        this.cccd = cccd;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
    }

    public String getMaNguoiDoc() { return maNguoiDoc; }
    public void setMaNguoiDoc(String maNguoiDoc) { this.maNguoiDoc = maNguoiDoc; }

    public String getTenNguoiDoc() { return tenNguoiDoc; }
    public void setTenNguoiDoc(String tenNguoiDoc) { this.tenNguoiDoc = tenNguoiDoc; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    @Override
    public String toString() {
        return tenNguoiDoc + " (" + maNguoiDoc + ")";
    }
}