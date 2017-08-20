package terzer.lt.scrollfragment;

import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        ImageAdapter adapter = new ImageAdapter(this, getList() /*Test*/);
        viewPager.setAdapter(adapter);

    }

    public List<Ball> getList(){
        List<Ball> list = new ArrayList<Ball>();

        for(int i = 0;i < 10;i++){
            Ball ball = new Ball(Color.GREEN, i);
            list.add(ball);
        }

        for(int i = 10;i < 20;i++){
            Ball ball = new Ball(Color.RED, i);
            list.add(ball);
        }

        return list;
    }

}
