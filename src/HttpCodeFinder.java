/**
 * Created by IvanovsV on 02/07/2015.
 */
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.io.*;

public class HttpCodeFinder {
    private Map<String, Integer> httpCodes = new TreeMap<>();
    private Pattern pattern = Pattern.compile("status=\"\\d{3}\"");

    public void scanSingleFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line; // line of document to be scanned
        String found;
        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                found = matcher.group();
                int count = httpCodes.containsKey(found) ? httpCodes.get(found)
                        : 0;
                httpCodes.put(found, count + 1);
            }
        }
        reader.close();
    }

    public void printMap() {
        for (String key : httpCodes.keySet()) {
            System.out.println(key + " " + httpCodes.get(key));
        }
    }

    public void scanAllFiles(File[] files) throws IOException {
        for (File f : files) {
            scanSingleFile(f);
        }
    }

    public static void main(String[] args) throws IOException {
        File dir = new File(".");
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xlg");
            }
        });

        HttpCodeFinder finder = new HttpCodeFinder();

        finder.scanAllFiles(files);
        finder.printMap();
    }
}
