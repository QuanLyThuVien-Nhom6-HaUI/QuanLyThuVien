package haui.nhom6.qlthuvien.ui.sach;

import haui.nhom6.qlthuvien.model.Sach;
import java.util.List;

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
        void updateSach(Sach sach);
        void deleteSach(String maSach);
        int getTotalPages();
    }
}