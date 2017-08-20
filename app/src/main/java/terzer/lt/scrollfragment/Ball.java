package terzer.lt.scrollfragment;

public class Ball {

    Color color;
    int position;

    public Ball(Color color, int position){
        this.color = color;
        this.position = position;
    }

    public int getDrawable(){
        return Color.getColor(color);
    }

    public int getPosition(){
        return position;
    }

}
