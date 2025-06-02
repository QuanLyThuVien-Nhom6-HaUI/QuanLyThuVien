package haui.nhom6.qlthuvien.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import haui.nhom6.qlthuvien.R;

public class FeedbackActivity extends AppCompatActivity {

    private EditText etFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);

        etFeedback = findViewById(R.id.etFeedback);
        Button btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);
        Button btnBack = findViewById(R.id.btnBack); // lấy nút quay lại

        btnSubmitFeedback.setOnClickListener(v -> submitFeedback());

        // Xử lý nút "Quay lại"
        btnBack.setOnClickListener(v -> finish());
    }

    // Xử lý gửi phản hồi
    private void submitFeedback() {
        String feedback = etFeedback.getText().toString().trim();

        if (TextUtils.isEmpty(feedback)) {
            Toast.makeText(this, "Vui lòng nhập phản hồi", Toast.LENGTH_SHORT).show();
        } else {
            sendFeedbackToDeveloper(feedback);
        }
    }

    private void sendFeedbackToDeveloper(String feedback) {
        // Gửi phản hồi (ví dụ: lưu vào server hoặc gửi email)
        Toast.makeText(this, "Cảm ơn bạn đã gửi phản hồi!", Toast.LENGTH_SHORT).show();
        finish();  // Đóng Activity sau khi gửi
    }
}
