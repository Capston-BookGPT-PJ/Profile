package com.example.meltingbooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private ImageButton kakaoLoginBtn;
    private ImageButton emailLoginBtn;

    private ImageView naverLogin;
    private ImageView facebookLogin;
    private ImageView appleLogin;
    private ImageView googleLogin;

    private TextView signupText;
    private TextView findAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 상태바 색상 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // 버튼 및 뷰 초기화
        kakaoLoginBtn = findViewById(R.id.kakaoLoginBtn);
        emailLoginBtn = findViewById(R.id.emailLoginBtn);

        naverLogin = findViewById(R.id.naver);
        facebookLogin = findViewById(R.id.facebook);
        appleLogin = findViewById(R.id.apple);
        googleLogin = findViewById(R.id.google);

        signupText = findViewById(R.id.signUp);
        findAccountText = findViewById(R.id.findAccount);

        // 카카오 로그인 버튼 클릭 이벤트
        kakaoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "카카오 로그인 클릭", Toast.LENGTH_SHORT).show();
                // TODO: 카카오 로그인 로직 구현
                Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        // 이메일 로그인 버튼 클릭 이벤트
        emailLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "이메일 로그인 클릭", Toast.LENGTH_SHORT).show();
                // TODO: 이메일 로그인 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        // 소셜 로그인 클릭 이벤트
        naverLogin.setOnClickListener(v -> Toast.makeText(this, "네이버 로그인 클릭", Toast.LENGTH_SHORT).show());
        facebookLogin.setOnClickListener(v -> Toast.makeText(this, "페이스북 로그인 클릭", Toast.LENGTH_SHORT).show());
        appleLogin.setOnClickListener(v -> Toast.makeText(this, "애플 로그인 클릭", Toast.LENGTH_SHORT).show());
        //googleLogin.setOnClickListener(v -> Toast.makeText(this, "구글 로그인 클릭", Toast.LENGTH_SHORT).show());

        // ✅ 구글 로그인 버튼 클릭 이벤트
        googleLogin.setOnClickListener(v -> {
            Toast.makeText(this, "구글 로그인 클릭", Toast.LENGTH_SHORT).show();

            // 1. 서버에서 구글 로그인 redirect URL 받아오기
            new Thread(() -> {
                try {
                    URL url = new URL("http://meltingbooks.o-r.kr:8080/auth/GOOGLE");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String googleAuthUrl = response.toString().replace("\"", ""); // 따옴표 제거

                    // 2. 메인 스레드에서 브라우저 열기
                    runOnUiThread(() -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleAuthUrl));
                        startActivity(intent);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(this, "구글 로그인 URL 요청 실패", Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });

        Uri data = getIntent().getData();
        if (data != null && "meltingbooks".equals(data.getScheme())) {
            String code = data.getQueryParameter("code");

            if (code != null) {
                // 서버에 code 넘겨서 JWT 발급 요청
                new Thread(() -> {
                    try {
                        URL url = new URL("http://meltingbooks.o-r.kr:8080/auth/GOOGLE/callback?code=" + code);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");

                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // JSON 파싱
                        JSONObject json = new JSONObject(response.toString());
                        String token = json.getString("token");
                        String userId = json.getString("id");

                        // 저장
                        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
                        prefs.edit().putString("token", token).putString("userId", userId).apply();

                        // 메인 화면 이동
                        runOnUiThread(() -> {
                            Intent intent = new Intent(this, FeedActivity.class);
                            startActivity(intent);
                            finish();
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(this, "콜백 처리 실패", Toast.LENGTH_SHORT).show()
                        );
                    }
                }).start();
            }
        }

        // 하단 링크 클릭 이벤트
        signupText.setOnClickListener(v -> {
            Toast.makeText(this, "회원가입 클릭", Toast.LENGTH_SHORT).show();
            // TODO: 회원가입 화면으로 이동
            Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
            startActivity(intent);
        });

        findAccountText.setOnClickListener(v -> {
            Toast.makeText(this, "계정 찾기 클릭", Toast.LENGTH_SHORT).show();
            // TODO: 계정 찾기 화면으로 이동
            Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
            startActivity(intent);
        });
    }
}
