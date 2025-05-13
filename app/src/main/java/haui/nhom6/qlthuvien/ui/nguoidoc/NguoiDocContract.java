package haui.nhom6.qlthuvien.ui.nguoidoc;

import java.util.List;
import haui.nhom6.qlthuvien.model.NguoiDoc;

public interface NguoiDocContract {
    interface View {
        void showNguoiDocList(List<NguoiDoc> list);
        void showError(String message);
        void onSuccess(String message);
        void onError(String message);
    }

    interface Presenter {
        void loadNguoiDocList();
        void updateNguoiDoc(NguoiDoc nguoiDoc);
        void deleteNguoiDoc(String maNguoiDoc);
    }
}
