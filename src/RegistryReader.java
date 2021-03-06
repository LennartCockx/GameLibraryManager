import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

class RegistryReader {

    public static String readRegistry(String key) {
        try {
            // Run reg query, then read output with StreamReader (internal class)
            Process process = Runtime.getRuntime().exec("reg query " + '"' + "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders" + '"' + " /v " + '"' + key + '"');

            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String output = reader.getResult();

            return output.substring(output.indexOf("REG_EXPAND_SZ") + 17, output.length() - 4);
        } catch (Exception e) {
            return null;
        }

    }

    static class StreamReader extends Thread {
        private final InputStream is;
        private final StringWriter sw = new StringWriter();

        public StreamReader(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            } catch (IOException e) {
                Thread.currentThread().getStackTrace();
            }
        }

        public String getResult() {
            return sw.toString();
        }
    }
}
