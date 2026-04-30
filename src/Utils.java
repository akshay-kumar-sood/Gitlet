import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static String readFile(File file) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));

    String content = new String(bytes, StandardCharsets.UTF_8);

    // 🔥 remove BOM if present
    if (content.startsWith("\uFEFF")) {
        content = content.substring(1);
    }

    return content.trim();
}    

  public static void writeFile(String path, String content) {
    try {
        FileWriter fw = new FileWriter(path);
        fw.write(content.trim());  // 🔥 remove garbage
        fw.close();
    } catch (IOException e) {
        System.out.println("Write error");
    }
}

    public static void writeObject(File file, Object obj) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(obj);
            out.close();
        } catch (IOException e) {
            System.out.println("Serialization error");
        }
    }

    public static <T> T readObject(File file, Class<T> expectedClass) {
    try {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        Object obj = in.readObject();
        in.close();
        return expectedClass.cast(obj);
    } catch (Exception e) {
        e.printStackTrace(); // 🔥 show real error
        return null;
    }
}
}