package haui.nhom6.qlthuvien;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Chuyển sang PhieuMuonActivity khi nhấn nút "Mượn"
        findViewById(R.id.btnMuon).setOnClickListener(v -> {
            startActivity(new Intent(this, PhieuMuonActivity.class));
        });
    }
}