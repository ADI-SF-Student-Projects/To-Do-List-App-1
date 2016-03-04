package example.michaelmuccio.com.todo_list_muccio;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;


public class Main2Activity extends AppCompatActivity {

    public static final String THINGTODO = "details";
    ArrayList<String> mDetails;
    ArrayAdapter<String> mToDeatailsAdap;
    EditText userDetailInput;
    ListView usersDetails;
    TextView titleChange;
    TextView emptyDeats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDetails = new ArrayList<>();
        mToDeatailsAdap = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_list_item_1, mDetails);

        usersDetails = (ListView) findViewById(R.id.userDetailList);

        userDetailInput = (EditText) findViewById(R.id.userInputDetails);
        usersDetails.setAdapter(mToDeatailsAdap);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userDetailInput.getText().toString().isEmpty()) {
                    Toast.makeText(Main2Activity.this, "Not a valid Detail", Toast.LENGTH_SHORT).show();
                } else {
                    mDetails.add(userDetailInput.getText().toString());
                    mToDeatailsAdap.notifyDataSetChanged();
                    userDetailInput.setText("");
                    changeEmptyDetailsView();
                }

            }
        });

        changeTitle(getIntent().getStringExtra(THINGTODO));
        strikeThrough();
        longPressDelete();

    }
    //add strike-through capability. Imported new widget and got code from StackOverflow
    private void  strikeThrough(){

        usersDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView v = (TextView) view;

                v.setPaintFlags(v.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }
        });

    }

    private void longPressDelete(){

        usersDetails.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                mDetails.remove(position);
                mToDeatailsAdap.notifyDataSetChanged();
                return true;

            }
        });

    }

    public String changeTitle(String title) {

        titleChange = (TextView) findViewById(R.id.detailsTitle);

        titleChange.setText(title);

        return title;

    }

    public void changeEmptyDetailsView() {

        emptyDeats = (TextView) findViewById(R.id.textView);

        if (mDetails != null) {
            Log.d("MainActivity2", "is null?");

            emptyDeats.setText("My Details: ");

        }

    }

    @Override
    public void onBackPressed() {
        sendDetailsListBack();
    }

    private void sendDetailsListBack(){
        Intent detailsIntent = getIntent();
        if (detailsIntent == null){
            Log.i("Main2", "Null Details");
            return;
        }

        detailsIntent.putStringArrayListExtra(MainActivity.DETAILS_KEY, mDetails);
        setResult(RESULT_OK, detailsIntent);
        finish();

    }

}

