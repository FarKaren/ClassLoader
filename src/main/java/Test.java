public class Test {
    private int x;

    public Test() {
        this.x = 10;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "Test{" +
                "x=" + x +
                '}';
    }
}
