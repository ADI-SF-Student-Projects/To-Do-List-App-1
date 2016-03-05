package example.michaelmuccio.com.todo_list_muccio;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Global Data types
    public static final int REQUEST_CODE = 16;
    public static final String DETAILS_KEY = "detailsKey";
    public static final String DATA_INDEX_KEY = "myDataIndexKey";
    public static final int ERROR_INDEX = -1;
    public static final String THINGTODO = "details";
    private TextView emptyList2;
    private TextView mCounter;
    private EditText userToDoInput;
    private ListView usersList;
    private ArrayAdapter<String> mtoDoListAdap;
    private ArrayList<ArrayList<String>> masterDataList;
    private ArrayList<String> mDataList;
    private ArrayList <String> emptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDataList = new ArrayList<>();
        masterDataList = new ArrayList<>();
        emptyList = new ArrayList<>();


        usersList = (ListView) findViewById(R.id.userToDoList);
        userToDoInput = (EditText) findViewById(R.id.userInput);

        //setting the Data Collection to the adaptor
        mtoDoListAdap = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, mDataList); //adaptor linked to my nested collection within master
        usersList.setAdapter(mtoDoListAdap);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userToDoInput.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Not a valid TO DO!", Toast.LENGTH_SHORT).show();
                } else {

                    addItemToListView();
                    mtoDoListAdap.notifyDataSetChanged();
                    userToDoInput.getText().clear();
                    changeEmptyList();
                    listItemCounter();
                }
            }
        });

        setOnClickListeners();
        longPressDelete();

    }

    private void addItemToListView(){

        String userInput = userToDoInput.getText().toString();
        mDataList.add(userInput);

    }

    private void setOnClickListeners() {

        final Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                masterDataList.add(emptyList);
                intent.putExtra(DATA_INDEX_KEY, position);
                intent.putExtra(DETAILS_KEY, masterDataList.get(position));
                intent.putExtra(THINGTODO, mDataList.get(position));
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

    }

    public void changeEmptyList() {

        emptyList2 = (TextView) findViewById(R.id.textView);

        if (masterDataList != null) {
            Log.d("MainActivity2", "is null?");

            emptyList2.setText("My List:");

        }
    }

    //add longpress for deleting the List item completely
    private void longPressDelete() {

        //the actual listview
        usersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //need to reference the linkedlist and adaptor
                masterDataList.remove(position);
                mtoDoListAdap.notifyDataSetChanged();
                return true; //default was false

            }
        });

    }

    // add counter for items in list
    public void listItemCounter() {

        mCounter = (TextView) findViewById(R.id.counterBox);

        mCounter.setText("Item count: " + String.valueOf(usersList.getCount()));
        /* getting an Integer from getCount then turning it into a string for the
        TextView
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    ArrayList<String> tempList = data.getStringArrayListExtra(DETAILS_KEY);
                    Log.i("DetailActivity", "Details list: " + DETAILS_KEY);
                    int index = data.getIntExtra(DATA_INDEX_KEY, ERROR_INDEX);
                    if (index != ERROR_INDEX) {
                        masterDataList.set(index, tempList);
                    } else {
                        Log.e("Main", "Index is not valid: " + index);
                    }
                }
            }

        }
    }
}
