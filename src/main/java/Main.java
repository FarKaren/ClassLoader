import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) {
        CustomClassLoader loader = new CustomClassLoader(new String[]{"."});
        try {
            Class<?> cl = Class.forName("Test", true, loader);
            Test t = (Test) cl.getDeclaredConstructor().newInstance();
            System.out.println(t);
        } catch (ClassNotFoundException | NoSuchMethodException
                | InvocationTargetException | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
