package haui.nhom6.qlthuvien;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import haui.nhom6.qlthuvien.database.DatabaseHelper;
import haui.nhom6.qlthuvien.utils.FeedbackActivity;

public class MainActivity extends AppCompatActivity {
    private boolean isPasswordVisible = false;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Apply window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        // UI elements
        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        ImageButton exitButton = findViewById(R.id.exitButton);
        ImageButton togglePasswordButton = findViewById(R.id.togglePassword);
        ImageButton smsButton = findViewById(R.id.smsButton);
        ImageButton introButton = findViewById(R.id.introButton);

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            String tenDangNhap = usernameEditText.getText().toString().trim();
            String matKhau = passwordEditText.getText().toString().trim();

            // Check if fields are empty
            if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập cả tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
            } else {
                // Check if username exists
                if (!dbHelper.kiemTraTenDangNhap(tenDangNhap)) {
                    Toast.makeText(this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                } else if (!dbHelper.kiemTraMatKhau(tenDangNhap, matKhau)) {
                    Toast.makeText(this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    String vaiTro = dbHelper.layVaiTro(tenDangNhap);
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    // Redirect based on role
                    if ("QuanLy".equalsIgnoreCase(vaiTro)) {
                        startActivity(new Intent(MainActivity.this, AdminActivity.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                    }
                }
            }
        });

        // Exit button click listener
        exitButton.setOnClickListener(v -> finish());

        // Toggle password visibility
        togglePasswordButton.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                togglePasswordButton.setImageResource(R.drawable.ic_eye_off);
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordButton.setImageResource(R.drawable.ic_eye);
            }
            passwordEditText.setSelection(passwordEditText.getText().length());
        });

        // SMS button click listener
        // Feedback button click listener
        smsButton.setOnClickListener(v -> {
            // Chuyển đến trang gửi phản hồi
            Intent feedbackIntent = new Intent(MainActivity.this, FeedbackActivity.class);
            startActivity(feedbackIntent);

            Log.d("MainActivity", "Feedback form opened.");
        });


        // Intro button click listener
        introButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close database connection when activity is destroyed
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
