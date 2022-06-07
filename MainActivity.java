package space.shades.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    EditText editTextIP;
    Button buttonConnect;
    TextView textViewData;

    ClientThread clientThread;
    DataHandler dataHandler;

    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        editTextIP = findViewById(R.id.eEditTextIP);
        buttonConnect = findViewById(R.id.eButtonConnect);
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        textViewData = findViewById(R.id.eTextViewData);

        dataHandler = new DataHandler(this);
    }

    View.OnClickListener buttonConnectOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View arg0)
        {
            clientThread = new ClientThread(editTextIP.getText().toString(), dataHandler);
            clientThread.start();
        }
    };

    private void updateData(String msg)
    {
        if (list.size() >= 10)
            list.remove(0);
        list.add(msg);
        String new_data = "";
        for (int i = list.size() - 1; i >= 0; --i)
            new_data += list.get(i) + "\n";
        textViewData.setText(new_data);
    }

    public static class DataHandler extends Handler
    {
        public static final int UPDATE_STATE = 0;
        public static final int UPDATE_MSG = 1;
        public static final int UPDATE_END = 2;
        private MainActivity parent;

        public DataHandler(MainActivity parent) {
            super();
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case UPDATE_STATE:
                    break;
                case UPDATE_MSG:
                    parent.updateData((String)msg.obj);
                    break;
                case UPDATE_END:
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
}