package example.michaelmuccio.com.todo_list_muccio;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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


public class DetailActivity extends AppCompatActivity {

    ArrayList<String> mDetailsList;
    ArrayList<String> tempList;
    ArrayAdapter<String> toDetailsAdap;
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

        usersDetails = (ListView) findViewById(R.id.userDetailList);
        userDetailInput = (EditText) findViewById(R.id.userInputDetails);
        titleChange = (TextView) findViewById(R.id.detailsTitle);
        //getDataIndex();

        mDetailsList = new ArrayList<>();

        getDataArrayList();

        Log.d("this is mdetail list", "Mdetaillist" + mDetailsList);

        toDetailsAdap = new ArrayAdapter<String>(DetailActivity.this, android.R.layout.simple_list_item_1, mDetailsList);
        usersDetails.setAdapter(toDetailsAdap);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userDetailInput.getText().toString().isEmpty()) {
                    Toast.makeText(DetailActivity.this, "Not a valid Detail", Toast.LENGTH_SHORT).show();
                } else {
                    addDetailToListView();
                    toDetailsAdap.notifyDataSetChanged();
                    userDetailInput.getText().clear();
                    changeEmptyDetailsView();
                }

            }
        });

        getMyTitle();
        strikeThrough();
        longPressDelete();

    }

    private void getMyTitle(){
        Intent titleIntent = getIntent();

        String newTitle = titleIntent.getStringExtra(MainActivity.THINGTODO);

        titleChange.setText(newTitle);

    }

    private int getDataIndex(){
        Intent getIndexIntent = getIntent();
        if (getIndexIntent == null){
            return MainActivity.ERROR_INDEX;
        }

        return getIndexIntent.getIntExtra(MainActivity.DATA_INDEX_KEY, MainActivity.ERROR_INDEX);

    }

    private void getDataArrayList(){
        Intent getDataIntent = getIntent();

        mDetailsList = getDataIntent.getStringArrayListExtra(MainActivity.DETAILS_KEY);
    }

    private void addDetailToListView(){

        String userInput = userDetailInput.getText().toString();
        mDetailsList.add(userInput);

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

                mDetailsList.remove(position);
                toDetailsAdap.notifyDataSetChanged();
                return true;

            }
        });

    }

    public void changeEmptyDetailsView() {

        emptyDeats = (TextView) findViewById(R.id.textView);

        if (mDetailsList != null) {
            Log.d("DetailActivity", "is null?");

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

        detailsIntent.putExtra(MainActivity.DETAILS_KEY, mDetailsList);
        detailsIntent.putExtra(MainActivity.DATA_INDEX_KEY, getDataIndex());
        setResult(RESULT_OK, detailsIntent);
        finish();

    }

}

