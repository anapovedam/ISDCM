/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import org.apache.xml.security.Init;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.keys.KeyInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

/**
 *
 * @author alumne
 */
public class XMLCryptoUtils {
     static {
        Init.init(); // Inicializa la librería de Apache XML Security
    }

    public static void encryptXML(String inputXmlPath, String outputXmlPath, String base64Key) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File(inputXmlPath));

        // Clave secreta AES
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // Elemento a cifrar (todo el contenido del primer <Item>)
        Node elementToEncrypt = document.getElementsByTagName("Item").item(0);

        XMLCipher cipher = XMLCipher.getInstance(XMLCipher.AES_128);
        cipher.init(XMLCipher.ENCRYPT_MODE, key);

        EncryptedData encryptedData = cipher.getEncryptedData();
        KeyInfo keyInfo = new KeyInfo(document);
        encryptedData.setKeyInfo(keyInfo);

        cipher.doFinal(document, (Element) elementToEncrypt, false);

        // Guardar XML cifrado
        writeXmlToFile(document, outputXmlPath);
    }

    public static void decryptXML(String inputXmlPath, String outputXmlPath, String base64Key) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File(inputXmlPath));

        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        Node encryptedDataElement = document.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptedData").item(0);

        XMLCipher cipher = XMLCipher.getInstance();
        cipher.init(XMLCipher.DECRYPT_MODE, key);

        cipher.doFinal(document, (Element) encryptedDataElement);

        writeXmlToFile(document, outputXmlPath);
    }

    private static void writeXmlToFile(Document doc, String filePath) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            transformer.transform(new DOMSource(doc), new StreamResult(fos));
        }
    }
}
