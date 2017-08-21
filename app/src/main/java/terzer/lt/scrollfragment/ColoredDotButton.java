package terzer.lt.scrollfragment;

public class ColoredDotButton {

    private ColoredDot dot;
    private float posX, posY;

    public ColoredDotButton(ColoredDot dot) {
        this.dot = dot;
    }

    public ColoredDot getDot() {
        return dot;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPos(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }
}
