package haui.nhom6.qlthuvien.ui.nhanvien;

import android.content.Context;
import java.util.List;
import haui.nhom6.qlthuvien.database.NhanVienDAO;
import haui.nhom6.qlthuvien.model.NhanVien;

public class NhanVienPresenter implements NhanVienContract.Presenter {
    private final NhanVienContract.View view;
    private final NhanVienDAO dao;

    public NhanVienPresenter(NhanVienContract.View view, Context context) {
        this.view = view;
        // Dùng applicationContext để DAO luôn được khởi tạo
        this.dao = new NhanVienDAO(context.getApplicationContext());
    }

    @Override
    public void loadNhanVienList() {
        List<NhanVien> list = dao.getAllNhanVien();
        if (list.isEmpty()) {
            view.showError("Không có nhân viên nào.");
        } else {
            view.showNhanVienList(list);
        }
    }

    @Override
    public void updateNhanVien(NhanVien nhanVien) {
        long r = dao.updateNhanVien(nhanVien);
        if (r > 0) view.onSuccess("Cập nhật thành công");
        else       view.onError("Cập nhật thất bại");
    }

    @Override
    public void deleteNhanVien(String maNhanVien) {
        int r = dao.deleteNhanVien(maNhanVien);
        if (r > 0) view.onSuccess("Xóa thành công");
        else       view.onError("Xóa thất bại");
    }
}
