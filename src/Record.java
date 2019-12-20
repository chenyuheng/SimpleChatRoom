import java.util.ArrayList;
import java.util.Date;

public class Record {
    private Date time;
    private String name;
    private String message;
    public static ArrayList<Record> records = new ArrayList<>();

    public Record(String name, String message) {
        this.name = name;
        this.message = message;
        this.time = new Date();
        records.add(this);
    }

    @Override
    public String toString() {
        if (new Date().getTime() - time.getTime() > 1000 * 60) {
            records.remove(this);
            return "";
        }
        return "{ \"name\":\"" + name + "\",\"message\":\"" + message + "\", \"time\":\"" + time + "\"}";
    }

    public static String getJSON() {
        if (records.size() == 0) {
            return "[]";
        }
        String json = "[";
        for (int i = 0; i < records.size() - 1; i++) {
            String record = records.get(i).toString();
            if (!record.equals("") && record != null) {
                json += record + ",";
            }
        }
        json += records.get(records.size() - 1);
        json += "]";
        return json;
    }
}
