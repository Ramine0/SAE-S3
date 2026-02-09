package utilitaire;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class OdgReader {
    public static String extractContentXml(File odgFile) throws IOException {
        try (ZipFile zipFile = new ZipFile(odgFile)) {

            ZipEntry entry = zipFile.getEntry("content.xml");
            if (entry == null) {
                throw new FileNotFoundException("content.xml introuvable dans le fichier ODG");
            }

            try (InputStream is = zipFile.getInputStream(entry);
                 BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                return sb.toString();
            }
        }
    }
    public static void printAllTextNodes(String xml) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));

        NodeList nodes = doc.getElementsByTagName("*");

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String text = node.getTextContent().trim();
                if (!text.isEmpty()) {
                    System.out.println(text);
                }
            }
        }
    }
}
