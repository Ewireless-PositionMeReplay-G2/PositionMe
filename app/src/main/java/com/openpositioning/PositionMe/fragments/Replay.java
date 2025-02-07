package com.openpositioning.PositionMe.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.protobuf.InvalidProtocolBufferException;
import com.openpositioning.PositionMe.IndoorMapManager;
import com.openpositioning.PositionMe.R;
import com.openpositioning.PositionMe.Traj.Trajectory;
import com.openpositioning.PositionMe.UtilFunctions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Replay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_replay);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // get Intent filename and path
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("fileName"); // 获取文件名
        String filePath = intent.getStringExtra("filePath"); // 获取文件路径


        // print log for file name and path
        Log.d("ReplayActivity", "Received fileName: " + fileName);
        Log.d("ReplayActivity", "Received filePath: " + filePath);

        // 读取并解析文件
        Trajectory trajectory = readTrajectoryFromFile(this, filePath);



        TextView textView = findViewById(R.id.textView2);
        textView.setText("Android版本: " + trajectory.getAndroidVersion() + "\n" +
                "起始时间戳: " + trajectory.getStartTimestamp() + "\n" +
                "GNSS：" + trajectory.getGnssDataList() + "\n" +
                "Position: " + trajectory.getPositionDataList());
    }



    public static Trajectory readTrajectoryFromFile(Context context, String filePath) {
//        File file = new File(context.getFilesDir(), filePath);

        File file = new File(filePath);
        if (!file.exists()) {
            Log.e(TAG, "文件不存在");
        }


        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return Trajectory.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            Log.e(TAG, "Protobuf 解析失败", e);
        } catch (IOException e) {
            Log.e(TAG, "文件读取失败", e);
        }
        return null;
    }


}


