package android.com.changyou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnLoginByQQ:
                break;
            case R.id.btnLoginUp:
                startActivity(new Intent(this,LoginUpActivity.class));
                overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                break;
            case R.id.btnLoginIn:
                startActivity(new Intent(this,LoginInActivity.class));
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                break;
            case R.id.imgBackButton:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
