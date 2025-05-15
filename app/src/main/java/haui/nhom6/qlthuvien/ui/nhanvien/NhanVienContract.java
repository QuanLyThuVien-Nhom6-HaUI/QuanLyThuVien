package haui.nhom6.qlthuvien.ui.nhanvien;

import java.util.List;
import haui.nhom6.qlthuvien.model.NhanVien;

public interface NhanVienContract {
    interface View {
        void showNhanVienList(List<NhanVien> list);
        void showError(String message);
        void onSuccess(String message);  // Thêm phương thức này
        void onError(String message);   // Thêm phương thức này
    }

    interface Presenter {
        void loadNhanVienList();
        void updateNhanVien(NhanVien nhanVien); // Thêm phương thức này
        void deleteNhanVien(String maNhanVien); // Thêm phương thức này
    }
}

