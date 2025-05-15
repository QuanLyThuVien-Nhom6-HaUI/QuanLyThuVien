package haui.nhom6.qlthuvien.ui.sach;

import java.util.List;

import haui.nhom6.qlthuvien.model.Sach;

public interface SachContract {
    interface View {
        void showSachList(List<Sach> list);
        void showError(String message);
        void onSuccess(String message);
        void onError(String message);
        void updatePageInfo(int currentPage, int totalPages);
    }

    interface Presenter {
        void loadSachList(int page);
        void timKiemSach(String tuKhoa);
        void updateSach(Sach sach);
        void deleteSach(String maSach);
        int getTotalPages();
    }
}