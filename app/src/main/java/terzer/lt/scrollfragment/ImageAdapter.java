package terzer.lt.scrollfragment;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 2017.08.20.
 */

public class ImageAdapter extends PagerAdapter {
    Context context;

    public List<Ball> list;

    public ImageAdapter(Context context, List<Ball> list){
        this.context=context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size()/2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((List<ImageView>) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        Log.println(Log.DEBUG, "Width:", "" + width);
        Log.println(Log.DEBUG, "How much:", "" + width/30);

        List<ImageView> ballImanges = new ArrayList<ImageView>();

        for(int i = 0;i < width/30;i++){

            if((i+1) <= list.size()) {

                ImageView imageView = new ImageView(context);

                imageView.setScaleX((float) 0.1);
                imageView.setScaleY((float) 0.1);

                Ball ball = list.get(i);

                imageView.setImageResource(ball.getDrawable());
                container.addView(imageView, i,0);
                ballImanges.add(imageView);
            }
            else{
                break;
            }

        }

        return ballImanges;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        List<ImageView> list = (List<ImageView>) object;

        for(ImageView image : list){
            container.removeView(image);
        }
    }
}
