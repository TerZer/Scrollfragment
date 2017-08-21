package terzer.lt.scrollfragment;

public enum DotColor {

    RED(0xFF0000),
    GREEN(0x00FF00);

    public final int color;

    DotColor(int color) {
        this.color = color | 0xFF000000;
    }
}
