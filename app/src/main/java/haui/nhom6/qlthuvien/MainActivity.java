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

public class MainActivity extends AppCompatActivity {
    private boolean isPasswordVisible = false;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        ImageButton exitButton = findViewById(R.id.exitButton);
        ImageButton togglePasswordButton = findViewById(R.id.togglePassword);
        ImageButton smsButton = findViewById(R.id.smsButton);
        ImageButton introButton = findViewById(R.id.introButton);

        loginButton.setOnClickListener(v -> {
            String tenDangNhap = usernameEditText.getText().toString().trim();
            String matKhau = passwordEditText.getText().toString().trim();

            if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập cả tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
            } else {
                if (!dbHelper.kiemTraTenDangNhap(tenDangNhap)) {
                    Toast.makeText(this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                } else if (!dbHelper.kiemTraMatKhau(tenDangNhap, matKhau)) {
                    Toast.makeText(this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    String vaiTro = dbHelper.layVaiTro(tenDangNhap);
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    if ("QuanLy".equalsIgnoreCase(vaiTro)) {
                        startActivity(new Intent(MainActivity.this, AdminActivity.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                    }
                }
            }
        });

        exitButton.setOnClickListener(v -> {
            finish();
        });

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

        smsButton.setOnClickListener(v -> {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.setData(Uri.parse("smsto:" + "0123456789"));
            smsIntent.putExtra("sms_body", "Xin chào, tôi cần hỗ trợ đăng nhập vào ứng dụng Quản Lý Thư Viện.");
            if (smsIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(smsIntent);
            } else {
                Log.d("MainActivity", "No SMS app found for Intent: " + smsIntent.toString());
                Toast.makeText(this, "Không tìm thấy ứng dụng nhắn tin. Vui lòng cài đặt ứng dụng nhắn tin.", Toast.LENGTH_SHORT).show();
            }
        });

        introButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}