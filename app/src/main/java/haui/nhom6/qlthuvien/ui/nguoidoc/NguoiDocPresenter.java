package haui.nhom6.qlthuvien.ui.nguoidoc;

import android.content.Context;

import java.util.List;

import haui.nhom6.qlthuvien.database.NguoiDocDAO;
import haui.nhom6.qlthuvien.model.NguoiDoc;

public class NguoiDocPresenter implements NguoiDocContract.Presenter {
    private final NguoiDocContract.View view;
    private final NguoiDocDAO dao;
    private static final int ITEMS_PER_PAGE = 10;

    public NguoiDocPresenter(Context context, NguoiDocContract.View view) {
        this.view = view;
        this.dao = new NguoiDocDAO(context.getApplicationContext());
    }

    @Override
    public void loadNguoiDocList(int page) {
        int offset = (page - 1) * ITEMS_PER_PAGE;
        List<NguoiDoc> list = dao.getNguoiDocByPage(offset, ITEMS_PER_PAGE);
        if (list.isEmpty()) {
            view.showError("Không có người đọc nào.");
        } else {
            view.showNguoiDocList(list);
        }
        int totalPages = getTotalPages();
        view.updatePageInfo(page, totalPages);
    }

    @Override
    public void timKiemNguoiDoc(String tuKhoa) {
        List<NguoiDoc> danhSachTimKiem;
        if (tuKhoa.trim().isEmpty()) {
            // Hiển thị toàn bộ nếu không có từ khóa
            danhSachTimKiem = dao.getAllNguoiDoc();
        } else {
            danhSachTimKiem = dao.timKiemNguoiDoc(tuKhoa);
        }
        view.showNguoiDocList(danhSachTimKiem);
    }

    @Override
    public void updateNguoiDoc(NguoiDoc nguoiDoc) {
        long result = dao.suaNguoiDoc(nguoiDoc);
        if (result > 0) {
            view.onSuccess("Cập nhật người đọc thành công");
        } else {
            view.onError("Cập nhật người đọc thất bại");
        }
    }

    @Override
    public void addNguoiDoc(NguoiDoc nguoiDoc) {
        long result = dao.themNguoiDoc(nguoiDoc);
        if (result > 0) {
            view.onSuccess("Thêm người đọc thành công");
            loadNguoiDocList(1); // Tải lại trang đầu tiên sau khi thêm
        } else {
            view.onError("Thêm người đọc thất bại");
        }
    }


    @Override
    public void deleteNguoiDoc(String maNguoiDoc) {
        int result = dao.xoaNguoiDoc(maNguoiDoc);
        if (result > 0) {
            view.onSuccess("Xóa người đọc thành công");
            loadNguoiDocList(1); // Tải lại trang đầu tiên sau khi xóa
        } else {
            view.onError("Xóa người đọc thất bại");
        }
    }

    @Override
    public int getTotalPages() {
        int totalNguoiDoc = dao.getTotalNguoiDocCount();
        return (int) Math.ceil((double) totalNguoiDoc / ITEMS_PER_PAGE);
    }
}