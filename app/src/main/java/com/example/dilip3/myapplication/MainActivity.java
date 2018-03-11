package com.example.dilip3.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    String listArray[];
    String listType[];
    PackageManager pm;
    List <ApplicationInfo> installedApps=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* String manufacturer = "xiaomi";
        if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            //this will open auto start screen where user can enable permission for your app
            Intent intent1 = new Intent();
            intent1.setComponent(new ComponentName("com.miui.securitycenter","com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent1);
        }*/
        pm=getPackageManager();

       List<ApplicationInfo> apps=pm.getInstalledApplications(0);

        for (ApplicationInfo app : apps){
            if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!=0){
                installedApps.add(app);

            }else if ((app.flags & ApplicationInfo.FLAG_SYSTEM)!=0){

            }
            else
                 installedApps.add(app);
        }


        String applist="", appType="";
        int safe=0,unknown=0;

        for(int i=0;i<installedApps.size();i++) {
            applist+=pm.getApplicationLabel(installedApps.get(i))+"#";
            if ("com.android.vending".equals(pm.getInstallerPackageName(installedApps.get(i).packageName)))
            {appType += "Play Store#";
                safe++;
            }
            else if ("com.google.android.packageinstaller".equals(pm.getInstallerPackageName(installedApps.get(i).packageName)) || pm.getInstallerPackageName(installedApps.get(i).packageName)==null) {
                appType += "Unknown Source#";
                unknown++;
            }
            else {
                appType += "System App#";
            }
        }

        listArray=applist.split("#");
        listType=appType.split("#");
        for(int i=0;i<listArray.length;i++){
            System.out.println("**outputlog**"+listArray[i]+" - "+pm.getInstallerPackageName(installedApps.get(i).packageName));
        }
       // pm.getDrawable(installedApps.get(i))+"#"

        TextView tvSafe=findViewById(R.id.tvsafecount);
        TextView tvUnknown=findViewById(R.id.tvunknowncount);
        TextView tvInstalledApplications=findViewById(R.id.tvInstalledApplications);
        int total=unknown+safe;
        tvInstalledApplications.setText("Installed Applications ("+total+")");
        tvSafe.setText(safe+ " Play Store Apps");
        tvUnknown.setText(unknown+" Unknown Source Apps");

        CustomAdapter customAdapter = new CustomAdapter();
        ListView bListView= findViewById(R.id.InstalledApplicationList);
        //ListAdapter bAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listArray);
        bListView.setAdapter(customAdapter);
        bListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tvAppName = view.findViewById(R.id.tvAppName);
                Toast.makeText(getApplicationContext(),tvAppName.getText(),Toast.LENGTH_SHORT).show();
              /*  Uri packageUri=Uri.parse(installedApps.get(0).packageName);
                Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
                startActivity(uninstallIntent);*/
              Vibrator v=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
              v.vibrate(10);
            }
        });

    }
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listArray.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_layout, null);
            TextView tvAppname=view.findViewById(R.id.tvAppName);
            TextView tvAppType=view.findViewById(R.id.tvAppType);
            tvAppname.setText(listArray[i]);
            tvAppType.setText(listType[i]);
           //Drawable Icon
                Drawable icon = pm.getApplicationIcon(installedApps.get(i));
                ImageView imgView = view.findViewById(R.id.imageView);
                imgView.setImageDrawable(icon);

            if(listType[i].contains("Unknown"))
            {tvAppType.setTextColor(Color.RED);
             //tvAppname.setTextColor(Color.RED);
            }
                else if(listType[i].contains("System"))
            {tvAppType.setTextColor(Color.GRAY);
            // tvAppname.setTextColor(Color.GRAY);
            }
            return view;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}

