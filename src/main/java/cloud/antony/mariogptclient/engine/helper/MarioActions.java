package cloud.antony.mariogptclient.engine.helper;

public enum MarioActions
{
    LEFT(0, "Left"), 
    RIGHT(1, "Right"), 
    DOWN(2, "Down"), 
    SPEED(3, "Speed"), 
    JUMP(4, "Jump");
    
    private int value;
    private String name;
    
    private MarioActions(final int value, final String name2) {
        this.value = value;
        this.name = name2;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public String getString() {
        return this.name;
    }
    
    public static int numberOfActions() {
        return values().length;
    }
    
    public static MarioActions getAction(final int n) {
        return values()[n];
    }
}
