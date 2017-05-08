package cpsproject.androidstudiosucks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewReplyFromServer;
    private EditText mEditTextSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSend = (Button) findViewById(R.id.btn_send);

        mEditTextSendMessage = (EditText) findViewById(R.id.edt_send_message);
        mTextViewReplyFromServer = (TextView) findViewById(R.id.tv_reply_from_server);

        buttonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_send:
                sendMessage(mEditTextSendMessage.getText().toString());
                try{
                    Context context = getApplicationContext();
                    Socket c = new Socket("137.140.181.126", 22);
                    String x = String.valueOf(c.isConnected());
                    System.out.println(c.isConnected());
                    Toast toast = Toast.makeText(context, x,Toast.LENGTH_LONG);
                    toast.show();
                    c.close();
                } catch (IOException e) {
            e.printStackTrace();
        }


                break;
        }
    }

    private void sendMessage(final String msg) {

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.
                    Context context = getApplicationContext();
                    Socket s = new Socket("137.140.183.226", 22);
                    String x = String.valueOf(s.isConnected());
                    Toast toast = Toast.makeText(context, x,Toast.LENGTH_LONG);
                    toast.show();
                    OutputStream out = s.getOutputStream();

                    PrintWriter output = new PrintWriter(out);

                    output.println(msg);
                    output.flush();
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    final String st = input.readLine();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String s = mTextViewReplyFromServer.getText().toString();
                            if (st.trim().length() != 0)
                                mTextViewReplyFromServer.setText(s + "\nFrom Server : " + st);
                        }
                    });

                    output.close();
                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


}


