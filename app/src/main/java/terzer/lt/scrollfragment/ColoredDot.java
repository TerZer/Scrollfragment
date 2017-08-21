package terzer.lt.scrollfragment;

import android.support.annotation.NonNull;

public class ColoredDot implements Comparable<ColoredDot> {

    private int number;
    private boolean enabled;
    private DotHandler handler;

    public ColoredDot(int number, boolean enabled, DotHandler handler) {
        this.number = number;
        this.enabled = enabled;
        this.handler = handler;
    }

    public DotColor getColor() {
        return enabled ? DotColor.GREEN : DotColor.RED;
    }

    @Override
    public int compareTo(@NonNull ColoredDot o) {
        return number - o.number;
    }

    public DotHandler getHandler() {
        return handler;
    }

    public int getNumber() {
        return number;
    }
}
