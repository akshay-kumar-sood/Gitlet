import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class Commit implements Serializable {

    private static final long serialVersionUID = 1L; // ✅ IMPORTANT

    String message;
    String timestamp;
    String parentId;
    HashMap<String, String> files;

    public Commit(String message, String parentId, HashMap<String, String> files) {
        this.message = message;
        this.parentId = parentId;
        this.files = files;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}