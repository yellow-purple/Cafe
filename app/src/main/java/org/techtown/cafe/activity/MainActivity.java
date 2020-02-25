package org.techtown.cafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.techtown.cafe.R;
import org.techtown.cafe.fragment.AdeFragment;
import org.techtown.cafe.fragment.BlendedFragment;
import org.techtown.cafe.fragment.CoffeeFragment;
import org.techtown.cafe.fragment.TeaFragment;
import org.techtown.cafe.util.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    String Tag="MainActivityConnection";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Socket socket;
    private ReceiveThread receive;
    Handler msghandler;



    Util utils=new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.pager);

        TabPagerAdapter pagerAdapter=new TabPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        SocketClient socketClient=new SocketClient();
        socketClient.start();
    }


    private class TabPagerAdapter extends FragmentPagerAdapter {
        private int tabCount;

        public TabPagerAdapter(@NonNull FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount=tabCount;

            msghandler=new Handler(){
                public void handleMessage(Message hdmsg){
                    if(hdmsg.what==1111){
                        String msg=hdmsg.obj.toString();

                    }
                }
            };

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    CoffeeFragment coffeeFragment=new CoffeeFragment();
                    return coffeeFragment;
                case 1:
                    BlendedFragment blendedFragment=new BlendedFragment();
                    return blendedFragment;
                case 2:
                    AdeFragment adeFragment=new AdeFragment();
                    return adeFragment;
                case 3:
                    TeaFragment teaFragment=new TeaFragment();
                    return teaFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==11){
            if(resultCode==RESULT_OK){
                String st=data.getStringExtra("result");
                Log.d(Tag,st);
                Toast.makeText(this,st,Toast.LENGTH_SHORT).show();

                SendThread sendThread=new SendThread(st);
                sendThread.start();


            }
        }
    }


    public void add_number(int num){
        ArrayList arrayList=utils.getIntArrayPref(this,"array");
        if(!arrayList.contains(num)){
        arrayList.add(num);
        Collections.sort(arrayList);
        utils.setIntArrayPref(this,"array",arrayList);
        }
    }


    class SocketClient extends Thread{
        boolean threadAlive;

        private DataOutputStream output=null;
        public SocketClient(){
            threadAlive=true;

        }
        public void run(){
            try{
                socket = new Socket("192.168.123.29", 5000);
                Log.d(Tag,"socket 접속 ");

                output = new DataOutputStream(socket.getOutputStream());
                receive = new ReceiveThread(socket);
                receive.start();

                output.writeUTF("Cafe");

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    class ReceiveThread extends Thread{
        private Socket socket;

        DataInputStream input;

        public ReceiveThread(Socket msocket){
            this.socket = msocket;
            try{
                input = new DataInputStream(socket.getInputStream());
            }catch (IOException e){
            }
        }
        public void run(){
            try{
                while(input !=null){
                    String msg =input.readUTF();
                    if(msg!=null){
                        Log.d(Tag,"receiveThread 실행");

                        Message hdmsg=msghandler.obtainMessage();
                        hdmsg.what=1111;
                        hdmsg.obj=msg;
                        msghandler.sendMessage(hdmsg);

                        Log.d(Tag,"Receive :"+hdmsg.obj.toString());

                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }



    class SendThread extends  Thread{
        String sendmsg;
        DataOutputStream output;

        public SendThread(String msg){
            sendmsg=msg;
            try{ output=new DataOutputStream(socket.getOutputStream());
            }catch (IOException e){

            }
        }

        public void run(){
            try{
                Log.d(Tag,"sendThread : "+sendmsg);

                if(output !=null){
                    output.writeUTF(sendmsg);
                }

            }catch(IOException e){
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SendThread sendthread=new SendThread("close");
        sendthread.start();
    }
}