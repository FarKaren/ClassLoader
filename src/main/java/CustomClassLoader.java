import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CustomClassLoader extends ClassLoader {

    private Map<String, Class<?>> cache = new HashMap<>();
    private String[] classPath;

    public CustomClassLoader(String[] classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        Class<?> result = findClass(name);
        if (resolve)
            resolveClass(result);
        return result;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        Class<?> result = cache.get(name);

        if (result != null)
            return result;

        File file = findFile(name.replace(".", "/"), ".class");
        if (file == null)
            return findSystemClass(name);


        byte[] byteCode;
        try {
            byteCode = loadFileAsBytes(file);
            result = defineClass(name, byteCode, 0, byteCode.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Cannot load file " + name + " : "+ e);
        }catch (ClassFormatError e){
            throw new ClassNotFoundException("Incorrect format of class file " + name + " : "+ e);
        }
        cache.put(name, result);
        return result;

    }

    private byte[] loadFileAsBytes(File file) throws IOException {
        byte[] byteCode = new byte[(int) file.length()];
        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(byteCode, 0, byteCode.length);
        }
        return byteCode;
    }

    @Override
    protected URL findResource(String name) {

        File file = findFile(name, "");
        if(file == null)
            return null;
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }

    }

    private File findFile(String name, String extension) {
        File file;

        for (int i = 0; i < classPath.length; i++) {
            file = new File((new File(classPath[i])
                    .getPath() + File.separatorChar + name.replace('/', File.separatorChar) + extension));
            if (file.exists())
                return file;
        }
        return null;
    }
}
