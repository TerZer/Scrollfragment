package terzer.lt.scrollfragment;

import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DotSliderView dotslider = (DotSliderView) findViewById(R.id.dotslider);

        //Mygtuko paspaudimas
        DotHandler handler = new DotHandler() {
            @Override
            public void onAction(ColoredDot dot) {
                Toast.makeText(MainActivity.this.getBaseContext(), "pressed " + dot.getNumber(), Toast.LENGTH_SHORT).show();
            }
        };

        //Test sukurimas
        Random random = new Random();
        List<ColoredDot> dots = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            dots.add(new ColoredDot(i, random.nextBoolean(), handler));
        }
        dotslider.setDots(dots);

    }

}
