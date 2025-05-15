package haui.nhom6.qlthuvien.ui.nguoidoc;

import android.content.Context;
import java.util.List;
import haui.nhom6.qlthuvien.database.NguoiDocDAO;
import haui.nhom6.qlthuvien.model.NguoiDoc;

public class NguoiDocPresenter implements NguoiDocContract.Presenter {
    private final NguoiDocContract.View view;
    private final NguoiDocDAO dao;

    public NguoiDocPresenter(NguoiDocContract.View view, Context context) {
        this.view = view;
        this.dao = new NguoiDocDAO(context.getApplicationContext());
    }

    @Override
    public void loadNguoiDocList() {
        List<NguoiDoc> list = dao.getAllNguoiDoc();
        if (list.isEmpty()) {
            view.showError("Không có người đọc nào.");
        } else {
            view.showNguoiDocList(list);
        }
    }

    @Override
    public void updateNguoiDoc(NguoiDoc nguoiDoc) {
        long r = dao.updateNguoiDoc(nguoiDoc);
        if (r > 0) view.onSuccess("Cập nhật thành công");
        else       view.onError("Cập nhật thất bại");
    }

    @Override
    public void deleteNguoiDoc(String maNguoiDoc) {
        int r = dao.deleteNguoiDoc(maNguoiDoc);
        if (r > 0) view.onSuccess("Xóa thành công");
        else       view.onError("Xóa thất bại");
    }
}

