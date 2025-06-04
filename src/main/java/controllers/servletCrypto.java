package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;

import util.CryptoUtils;
import util.SecretKeyUtil;
import javax.crypto.SecretKey;
/**
 *
 * @author alumne
 */
@WebServlet("/CryptoServlet")
public class servletCrypto extends HttpServlet {

    private final String basePath = "/home/alumne/NetBeansProjects/webAppISDCM/xmlcypher";
    //private final String encryptionKey = "CLAVE_BASE64";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String filename = request.getParameter("filename");

        if (action == null || filename == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros insuficientes.");
            return;
        }

        File inputFile = new File(basePath + filename);
        File outputFile;

        try {
            SecretKey secretKey = SecretKeyUtil.getSecretKey("AES");
            String encryptionKey = SecretKeyUtil.keyToString(secretKey);
            if ("encrypt".equals(action)) {
                outputFile = new File(basePath + "encrypted_" + filename);
                CryptoUtils.encryptFile(inputFile, outputFile, encryptionKey);
                response.getWriter().write("Archivo cifrado exitosamente: " + outputFile.getName());

            } else if ("decrypt".equals(action)) {
                outputFile = new File(basePath + "decrypted_" + filename);
                CryptoUtils.decryptFile(inputFile, outputFile, encryptionKey);
                response.getWriter().write("Archivo descifrado exitosamente: " + outputFile.getName());

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida.");
            }

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar el archivo: " + e.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}