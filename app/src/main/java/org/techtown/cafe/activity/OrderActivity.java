package org.techtown.cafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.cafe.R;
import org.techtown.cafe.util.Util;

import java.util.ArrayList;
import java.util.Collections;


public class OrderActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView temperatureView;
    private TextView menu_nameView;
    private EditText add_order;
    private Spinner spinner;
    private Button order_btn;

    private String name;
    private String temperature;
    private String extraString;

    private ArrayList<Integer> numList=new ArrayList<>();
    private Util utils=new Util();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        checkFirstRun();//첫실행시에만 번호 리스트 추가

        if(utils.getIntArrayPref(this,"array")==null) {
            Toast.makeText(this, "주문가능한 번호가 남아있지 않습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            numList = utils.getIntArrayPref(this, "array");
            Collections.sort(numList);
        }

        intent=getIntent();
        String data=intent.getStringExtra("name");

        toolbar=findViewById(R.id.toolbar);
        spinner=findViewById(R.id.spinner);
        temperatureView=findViewById(R.id.temperature);
        menu_nameView=findViewById(R.id.name);
        add_order=findViewById(R.id.add_order);
        order_btn=findViewById(R.id.order_btn);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        temperature=data.substring(0,3);
        name=data.substring(3);
        temperatureView.setText(name);
        menu_nameView.setText(temperature);

        ArrayAdapter arrayAdapter= new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,numList);
        spinner.setAdapter(arrayAdapter);


        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extraString=add_order.getText().toString();

                JSONObject str=setJsonData(Integer.parseInt(spinner.getSelectedItem().toString()),name,temperature,extraString);
                intent.putExtra("result",str.toString());
                setResult(RESULT_OK,intent);



                numList.remove(spinner.getSelectedItem());//해당 리스트 값 지우기
                utils.setIntArrayPref(getApplicationContext(),"array",numList);

                finish();
            }
        });

    }

    public JSONObject setJsonData(int num, String name,String temperature,String message){
//        Toast.makeText(getApplicationContext(),num+name+temperature+message,Toast.LENGTH_SHORT).show();
        JSONObject obj=new JSONObject();
        try {
            obj.put("number",num);
            obj.put("temperature",temperature);
            obj.put("menu",name);
            obj.put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addAllItem(){
        int[] num_array=getResources().getIntArray(R.array.num_array);
        for(int num:num_array){
            numList.add(num);
        }
        utils.setIntArrayPref(this,"array",numList);
    }


    public void checkFirstRun(){
        SharedPreferences prefs=getSharedPreferences("check_first",MODE_PRIVATE);

        boolean isFirstRun=prefs.getBoolean("isFirstRun",true);

        if(isFirstRun){
            prefs.edit().putBoolean("isFirstRun",false).apply();
            Toast.makeText(this, "첫번째 실행 ",Toast.LENGTH_SHORT).show();
            addAllItem();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
