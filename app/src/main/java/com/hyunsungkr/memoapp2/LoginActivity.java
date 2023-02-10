package com.hyunsungkr.memoapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyunsungkr.memoapp2.api.NetworkClient;
import com.hyunsungkr.memoapp2.api.UserApi;
import com.hyunsungkr.memoapp2.config.Config;
import com.hyunsungkr.memoapp2.model.User;
import com.hyunsungkr.memoapp2.model.UserRes;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    Button btnLogin;
    TextView txtRegister;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editEmail = findViewById(R.id.editTitle);
        editPassword = findViewById(R.id.editContent);
        btnLogin = findViewById(R.id.btnSave);
        txtRegister = findViewById(R.id.txtLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                if(pattern.matcher(email).matches()==false){
                    Toast.makeText(LoginActivity.this,"이메일 형식이 올바르지않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                // 비밀번호 체크
                String password = editPassword.getText().toString().trim();
                if(password.length()<4 || password.length()>12){
                    Toast.makeText(LoginActivity.this, "비밀번호 길이를 확인하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgress("로그인 중입니다...");

                Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
                UserApi api = retrofit.create(UserApi.class);

                User user = new User(email,password);

                Call<UserRes> call = api.login(user);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        dismissProgress();
                        if(response.isSuccessful()){

                            UserRes res = response.body();

                            // 억세스 토큰을 api할 때마다 헤더에서 사용하므로
                            // 회원가입이나 로그인이 끝나면 파일로 꼭 저장해놔한다.
                            SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCE_NAME,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(Config.ACCESS_TOKEN,res.getAccess_token());
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                            startActivity(intent);

                            finish();

                        }else if(response.code() == 400){

                            Toast.makeText(LoginActivity.this, "회원가입이 되어있지 않거나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        dismissProgress();
                        Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    // 네트워크 로직 처리 시에 화면에 보여주는 함수
    void showProgress(String message){
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }
    // 로직처리가 끝나면 화면에서 사라지는 함수
    void dismissProgress(){
        dialog.dismiss();
    }

}