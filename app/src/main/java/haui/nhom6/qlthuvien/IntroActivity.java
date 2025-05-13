package haui.nhom6.qlthuvien;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button backButton = findViewById(R.id.backButton);

        // Xử lý sự kiện nhấn nút Quay Lại
        backButton.setOnClickListener(v -> {
            finish(); // Quay lại MainActivity
        });
    }
}
