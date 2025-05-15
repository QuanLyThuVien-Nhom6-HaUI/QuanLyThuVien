package haui.nhom6.qlthuvien.model;

public class SachDaChon {
    private Sach sach;
    private int soLuong;

    public SachDaChon(Sach sach, int soLuong) {
        this.sach = sach;
        this.soLuong = soLuong;
    }

    public Sach getSach() { return sach; }
    public void setSach(Sach sach) { this.sach = sach; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}