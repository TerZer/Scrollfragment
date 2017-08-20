package terzer.lt.scrollfragment;

/**
 * Created by Home on 2017.08.20.
 */

public enum Color{
    RED, GREEN;

    public static int getColor(Color color){
        return (color == RED) ? R.drawable.red : R.drawable.green;
    }


}
