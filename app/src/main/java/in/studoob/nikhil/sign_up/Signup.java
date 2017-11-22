package in.studoob.nikhil.sign_up;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {


    private EditText signup_name;
    private EditText signup_email;
    private EditText signup_phone;
    private EditText signup_password;
    private Button  signup_btn;
    private TextView tv_login;
    public ProgressBar signup_pgr;
    private static String REGISTER_URL="http://makesimple.in/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup_name = (EditText) findViewById(R.id.signup_editText_name);
        signup_email = (EditText) findViewById(R.id.signup_editText_email);
        signup_phone = (EditText) findViewById(R.id.signup_editText_phone);
        signup_password = (EditText) findViewById(R.id.signup_editText_Password);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        tv_login=(TextView)findViewById(R.id.login_tv);
        signup_pgr=(ProgressBar)findViewById(R.id.signup_pgr);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req_login(REGISTER_URL);
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin=new Intent(Signup.this,Login.class);
                startActivity(intentLogin);
            }
        });
    }

    private void req_login(String url) {
        String signup_url=url;
        signup_pgr.setVisibility(View.VISIBLE);

        final String username = signup_name.getText().toString().trim();
        final String email = signup_email.getText().toString().trim();
        final String password = signup_password.getText().toString().trim();
        final String phone = signup_phone.getText().toString().trim();

        String urlSuffix = "?username=" + username + "&password=" + password + "&email=" + email+ "&phone=" + phone;
        url+=urlSuffix;
        Log.v("url",url);
        if (email.isEmpty()| password.isEmpty()) {
            Toast.makeText(getApplication(), "Enter complete details", Toast.LENGTH_SHORT).show();
            signup_pgr.setVisibility(View.INVISIBLE);
            return;
        } else {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            signup_pgr.setVisibility(View.INVISIBLE);
                            if (response.trim().equalsIgnoreCase("successfully registered")) {
                                Toast.makeText(getApplication(), "Signup Complete", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Signup.this, Login.class);
                                startActivity(intent);
                                finish();

                            }
                            else if(response.trim().equalsIgnoreCase("email already exist")){
                                Toast.makeText(getApplication(), "email already exist", Toast.LENGTH_SHORT).show();
                            }
                            else if(response.trim().equalsIgnoreCase("oops! Please try again!")){
                                Toast.makeText(getApplication(), "oops! Please try again!", Toast.LENGTH_SHORT).show();
                            }

                            else{
                                Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            signup_pgr.setVisibility(View.INVISIBLE);
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
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}
