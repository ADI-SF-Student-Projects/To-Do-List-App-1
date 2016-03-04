package example.michaelmuccio.com.todo_list_muccio;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import java.util.Collections;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    LinkedList<String> mtoDoList;
    ArrayAdapter<String> mtoDoListAdap;
    EditText userToDoInput;
    ListView usersList;
    public static final int REQUEST_CODE = 16;
    public static final String DETAILS_KEY = "detailsKey";
    TextView emptyList;
    TextView mCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mtoDoList = new LinkedList<>();
        mtoDoListAdap = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mtoDoList);

        usersList = (ListView) findViewById(R.id.userToDoList);

        userToDoInput = (EditText) findViewById(R.id.userInput);
        usersList.setAdapter(mtoDoListAdap);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userToDoInput.getText().toString().isEmpty()) {

                    Toast.makeText(MainActivity.this, "Not a valid TO DO!", Toast.LENGTH_SHORT).show();
                } else {

                    mtoDoList.add(userToDoInput.getText().toString());
                    mtoDoListAdap.notifyDataSetChanged();
                    userToDoInput.setText("");
                    changeEmptyList();
                    listItemCounter();
                }

            }
        });


        setOnClickListeners();
        longPressDelete();

    }

    private void setOnClickListeners() {

        final Intent intent = new Intent(MainActivity.this, Main2Activity.class);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intent.putExtra("details", mtoDoList.get(position));
                startActivityForResult(intent, 16);

            }
        });

    }

    public void changeEmptyList() {

        emptyList = (TextView) findViewById(R.id.textView);

        if (mtoDoList != null) {
            Log.d("MainActivity2", "is null?");

            emptyList.setText("My List:");

        }
    }

    //add longpress for deleting the List item completely
    private void longPressDelete() {

        //the actual listview
        usersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //need to reference the linkedlist and adaptor
                mtoDoList.remove(position);
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
        if (resultCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    data.getStringArrayListExtra(DETAILS_KEY);
                }
            }

        }
    }
}
