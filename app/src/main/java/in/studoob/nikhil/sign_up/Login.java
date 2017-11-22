package in.studoob.nikhil.sign_up;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    public static final String LOGIN_URL="http://makesimple.in/login.php";
    public static final String KEY_EMAIL="email";
    public static final String KEY_PASSWORD="password";
    public static final String LOGIN_SUCCESS="success";
    public static final String SHARED_PREF_NAME="nik";
    public static final String EMAIL_SHARED_PREF="email";
    public static final String LOGGEDIN_SHARED_PREF="loggedin";


    private EditText login_editTextEmail;
    private EditText login_editTextPassword;
    private ProgressBar login_pgr;
    private Button login;
    private TextView signup_tv;
    private boolean loggedIn=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_editTextEmail=(EditText)findViewById(R.id.login_email);
        login_editTextPassword=(EditText)findViewById(R.id.login_Password);
        login=(Button)findViewById(R.id.login_btn);
        signup_tv=(TextView)findViewById(R.id.signup_tv);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req_login();
            }
        });
        signup_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }



    private void req_login() {
        final String email = login_editTextEmail.getText().toString().trim();
        final String password = login_editTextPassword.getText().toString().trim();
        if (email.isEmpty()| password.isEmpty()) {
            Toast.makeText(getApplication(), "Enter complete details", Toast.LENGTH_SHORT).show();
            return;
        } else {
            login_pgr=(ProgressBar)findViewById(R.id.login_pgr);
            login_pgr.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equalsIgnoreCase(LOGIN_SUCCESS)) {

                                SharedPreferences sharedPreferences = Login.this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(LOGGEDIN_SHARED_PREF, true);
                                editor.putString(EMAIL_SHARED_PREF, email);
                                editor.commit();
                                Toast.makeText(getApplication(), "Login Complete", Toast.LENGTH_SHORT).show();
                                login_pgr.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("email",email);
                                startActivity(intent);
                                finish();

                            } else {
                                login_pgr.setVisibility(View.INVISIBLE);
                                Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            login_pgr.setVisibility(View.INVISIBLE);
                            String message = null;
                            if (error instanceof NetworkError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                                Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                message = "The server could not be found. Please try again after some time!!";
                                Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                message = "Authentication Error!!";
                                Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                message = "Parsing error! Please try again after some time!!";
                                Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                            } else if (error instanceof TimeoutError) {
                                message = "Connection TimeOut! Please check your internet connection.";
                                Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                            }
                        }

                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> prams = new HashMap<>();
                    prams.put(KEY_EMAIL, email);
                    prams.put(KEY_PASSWORD, password);
                    return prams;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}
