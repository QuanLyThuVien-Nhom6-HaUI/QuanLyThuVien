package haui.nhom6.qlthuvien.ui.sach;

import android.content.Context;
import haui.nhom6.qlthuvien.database.SachDAO;
import haui.nhom6.qlthuvien.model.Sach;
import java.util.List;

public class SachPresenter implements SachContract.Presenter {
    private final SachContract.View view;
    private final SachDAO dao;
    private static final int ITEMS_PER_PAGE = 10; // Số sách trên mỗi trang

    public SachPresenter(SachContract.View view, Context context) {
        this.view = view;
        this.dao = new SachDAO(context.getApplicationContext());
    }

    @Override
    public void loadSachList(int page) {
        int offset = (page - 1) * ITEMS_PER_PAGE;
        List<Sach> list = dao.getSachByPage(offset, ITEMS_PER_PAGE);
        if (list.isEmpty()) {
            view.showError("Không có sách nào.");
        } else {
            view.showSachList(list);
        }
        int totalPages = getTotalPages();
        view.updatePageInfo(page, totalPages);
    }

    @Override
    public void updateSach(Sach sach) {
        long result = dao.suaSach(sach);
        if (result > 0) {
            view.onSuccess("Cập nhật sách thành công");
        } else {
            view.onError("Cập nhật sách thất bại");
        }
    }

    @Override
    public void deleteSach(String maSach) {
        int result = dao.xoaSach(maSach);
        if (result > 0) {
            view.onSuccess("Xóa sách thành công");
            loadSachList(1); // Tải lại trang đầu tiên sau khi xóa
        } else {
            view.onError("Xóa sách thất bại");
        }
    }

    @Override
    public int getTotalPages() {
        int totalSach = dao.getTotalSachCount();
        return (int) Math.ceil((double) totalSach / ITEMS_PER_PAGE);
    }
}