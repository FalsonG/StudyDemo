package eason.falcon.studydemo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;

import eason.falcon.customView.GestureLocakViewGroup;

public class MainActivity extends AppCompatActivity {

    private GestureLocakViewGroup mGestureLockViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGestureLockViewGroup = (GestureLocakViewGroup) findViewById(R.id.id_gestureLockViewGroup);
        ArrayList<Integer> answers = new ArrayList<>();
        SharedPreferences sharedPreferences=getSharedPreferences("test",MODE_APPEND);
        String keys=sharedPreferences.getString("vales","");
        if(!TextUtils.isEmpty(keys))
        {
            String[] strs=keys.split(",");
            for(int i=0,len=strs.length;i<len;++i)
            {
                answers.add(Integer.parseInt(strs[i]));
            }
        }
        mGestureLockViewGroup.setAnswer(answers);
        mGestureLockViewGroup
                .setGestureLockViewListener(new GestureLocakViewGroup.OnGestureLockViewListener() {

                    @Override
                    public void onUnmatchedExceedBoundary() {
                        Toast.makeText(MainActivity.this, "错误5次...",
                                Toast.LENGTH_SHORT).show();
                        mGestureLockViewGroup.setUnMatchExceedBoundary(5);
                    }

                    @Override
                    public void onGestureEvent(boolean matched) {
                        Toast.makeText(MainActivity.this, matched + "",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBlockSelected(int cId) {
                    }

                    @Override
                    public void setGestureSuccess()
                    {

                    }

                    @Override
                    public void setGestureFailure()
                    {

                    }
                });
    }
    private void showNotifyCation()
    {
    }
}

