import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;

class Game implements Serializable {

    //global variables
    private static final long serialVersionUID = 5273081191464727478L;
    final String directory;
    private final String FolderName;
    String saveLocation = "";
    private String GameTitle;
    private String ID;
    private String Deck;
    private String cover;
    private String size;
    private File exe;

    //methods
    public Game(String FolderName, String directory) {
        this.directory = directory;
        this.FolderName = FolderName;
        getInfo();
    }

    private static String getCharacterDataFromElement(Element e) {
        if (e != null) {
            Node child = e.getFirstChild();
            if (child instanceof CharacterData) {
                CharacterData cd = (CharacterData) child;
                return cd.getData();
            }
        }
        return "";
    }

    private static long folderSize(File directory) {
        long length = 0;
        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.isFile())
                    length += file.length();
                else
                    length += folderSize(file);
            }
        }
        return length;
    }

    public void DatatbaseInfo() {
        String[] test = null;
        if (ID != null) {
            new Database_controller();
            test = Database_controller.showDatabaseContent(Long.parseLong(ID), directory);
        }
        if (test == null)
            exe = new File(directory);
        else {
            exe = new File(directory + test[0]);
            saveLocation = test[1];
        }
    }

    private String getInternetData(String url) {
        try {
            URL obj = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(obj.openStream()));


            StringBuilder response = new StringBuilder();
            String InputLine;

            while ((InputLine = in.readLine()) != null) {
                response.append(InputLine);
            }
            in.close();


            return response.toString();
        } catch (Exception x) {
            x.printStackTrace();
            return "";
        }

    }

    public void getInfo() {
        long temp;
        temp = folderSize(new File(directory));

        if (temp < 1000) {
            size = temp + " B";
        }
        if (temp < 1000000 && temp >= 1000) {
            temp = Math.round(temp / 10.00);
            size = (temp / 100.00) + " KB";
        }
        if (temp < 1000000000 && temp >= 1000000) {
            temp = Math.round(temp / 10000.00);
            size = (temp / 100.00) + " MB";
        }
        if (temp >= 1000000000) {
            temp = Math.round(temp / 10000000.00);
            size = (temp / 100.00) + " GB";
        }

        String xml;

        xml = getInternetData("http://www.giantbomb.com/api/search/?api_key=f762f684241dc2aee6ba170cfe09e31165a39a44&format=xml&query=" + FolderName + "&limit=1&resources=game&field_list=name,id,deck,image");

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("game");

            Element element = (Element) nodes.item(0);

            if (element != null) {
                NodeList name = element.getElementsByTagName("name");
                Element line = (Element) name.item(0);

                GameTitle = getCharacterDataFromElement(line);

                name = element.getElementsByTagName("id");
                line = (Element) name.item(0);

                ID = getCharacterDataFromElement(line);

                name = element.getElementsByTagName("deck");
                line = (Element) name.item(0);

                Deck = getCharacterDataFromElement(line);

                name = element.getElementsByTagName("small_url");
                line = (Element) name.item(0);

                cover = getCharacterDataFromElement(line);

                savepicture();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }

        DatatbaseInfo();
    }

    public String getImage() {
        return cover;
    }

    public String getGameTitle() {
        if (GameTitle == null) {
            return "Unknown Game";
        }
        return GameTitle;
    }

    public void setGameTitle(String GameTitle) {
        this.GameTitle = GameTitle;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void savepicture() {
        new SaveData().savepicture(cover, ID);
    }

    public String getFolderName() {
        return FolderName;
    }

    public String getDescription() {
        return Deck;
    }

    public File getExe() {
        return exe;
    }

    public void setExe(String path) {
        exe = new File(path);

        String temp = path.substring(directory.length());

        if (!temp.equals("") && !saveLocation.equals(""))
            Database_controller.editContent(Long.parseLong(ID), temp, saveLocation, directory);
    }

    public String getSaveLocation() {
        return saveLocation;
    }

    public void setSaveLocation(String saveLocation) {
        this.saveLocation = saveLocation;
    }

    public void setDeck(String Deck) {
        this.Deck = Deck;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSize() {
        return size;
    }
}
