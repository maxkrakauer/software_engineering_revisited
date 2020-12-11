package com.example.shoppinglists;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StoreItems extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button _show_item_list, _create_new_item, _add_new_item;
    ListView _lv;
    LinearLayout _layout;
    EditText _edit_name, _edit_price, _edit_amount, _edit_desc;
    String _user_id,_list_id;
    TextView _full_item_view;
    ArrayList <list_item> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_items);
/*
        btnUp = (Button) findViewById(R.id.btnUp);
        button = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.layout);
*/
        /*
        String passedArg = getIntent().getExtras().getString("arg");
enteredValue.setText(passedArg);
         */

        _user_id = getIntent().getExtras().getString("user_id");
        _list_id = getIntent().getExtras().getString("list_id");

        System.out.println("user id is:"+ _user_id);
        System.out.println("list id is: "+_list_id);

        _edit_name = (EditText) findViewById(R.id.new_name_edit);
        _edit_price = (EditText) findViewById(R.id.new_price_edit);
        _edit_amount = (EditText) findViewById(R.id.new_amount_edit);
        _edit_desc = (EditText) findViewById(R.id.new_desc_edit);

        _add_new_item = (Button) findViewById(R.id.add_new_item);
        _create_new_item = (Button) findViewById(R.id.create_new_item);
        _show_item_list = (Button) findViewById(R.id.show_item_list);
        _layout = (LinearLayout) findViewById(R.id.create_item_box);

        database = FirebaseDatabase.getInstance();
        myRef= database.getReference();

        _lv = (ListView) findViewById(R.id.list_view);


/*
        _add_new_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _layout.setVisibility(View.VISIBLE);
            }
        });

*/


        // addUsers();
        _create_new_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = _edit_name.getText().toString();
                String price = _edit_price.getText().toString();
                String desc = _edit_desc.getText().toString();
                String amount = _edit_amount.getText().toString();
                //Users u = new Users (name,price);
                list_item u= new list_item(name,desc,price,amount);
                //myRef.child("users").child(u.getName()).setValue(u);//name??
                //myRef.child("users").child(_user_id).child("lists").child(_list_id).child(u.get_name()).setValue(u);//name??
                myRef.child("users").child(_user_id).child("lists").child(_list_id).child(u.get_name()).setValue(u);//name??
                _layout.setVisibility(View.GONE);
            }
        });

        _show_item_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = myRef.child("store_items");
                query.addValueEventListener(new ValueEventListener() {//לשנות להקשבה חד פעמית
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users.clear();
                        for (DataSnapshot dst : snapshot.getChildren()) {
                            list_item u = dst.getValue(list_item.class);
                            users.add(u);
                        }
                        refresh_recycler();//?
                        // Toast.makeText(MainActivity.this, "555", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        Query query = myRef.child("store_items");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                    for (DataSnapshot dst : snapshot.getChildren()) {
                        list_item u = dst.getValue(list_item.class);
                        users.add(u);
                }
                refresh_recycler();//?
                // Toast.makeText(MainActivity.this, "555", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
                ViewGroup rootView1 = (ViewGroup) findViewById(R.id.list_item_layout);
                ViewGroup rootView2 = (ViewGroup) findViewById(R.id.listview);
// Start recording changes to the view hierarchy
                TransitionManager.beginDelayedTransition(rootView1);
                TransitionManager.beginDelayedTransition(rootView2);
                */
/*
                _full_item_view = (TextView) findViewById(R.id.full_item_view);

                String text = "name:\n"+users.get(position)._name+"\ndesc:\n"+users.get(position)._desc+
                        "\nprice:\n"+users.get(position)._price;
                _full_item_view.setText(text);
*/

                _layout.setVisibility(View.VISIBLE);
                _edit_name.setText(users.get(position).get_name());
                if(users.get(position)._amount!=null && !users.get(position)._amount.equals(""))
                _edit_amount.setText(users.get(position).get_amount());
                else _edit_amount.setText("");
                if(users.get(position)._desc!=null && !users.get(position)._desc.equals(""))
                _edit_desc.setText(users.get(position).get_desc());
                else _edit_desc.setText("");
                if(users.get(position)._price!=null && !users.get(position)._price.equals(""))
                _edit_price.setText(users.get(position).get_price());
                else _edit_price.setText("");



            }
        });



    }
    /*
    private void addUsers(){
        for(int i=1; i<100; i++){
            Users users = new Users("Name"+i, "shekls"+i );
            myRef.child("list").child(users.getName()).setValue(users);
        }
    }
     */

    private void refresh_recycler(){

        shop_adapter adapter = new shop_adapter(this, R.layout.list_item, users);
        _lv.setAdapter(adapter);
    }



}