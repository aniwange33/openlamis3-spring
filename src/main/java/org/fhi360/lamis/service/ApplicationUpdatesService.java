/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;

/**
 * @author user1
 */

import org.apache.commons.io.FileUtils;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.DateUtil;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class ApplicationUpdatesService {
    private static String contextPath;
    private final ApplicationProperties applicationProperties;

    public ApplicationUpdatesService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    //This method reads the version manifest on the server and compares the date of manifest sent by the client
    //and tokenize all files names on the server manifest whose modification date is after the last clients date of manifest 
    public String getManifestToken(String dateManifest, String dateSchema) {
        contextPath = applicationProperties.getContextPath();

        String token = "";
        try {
            File file = new File(contextPath + "version/manifest.txt");
            if (file.exists()) {
                InputStream input = new FileInputStream(file);
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    String split[] = line.split("#");
                    if (!line.contains("Manifest-Date")) {
                        if (!line.contains("Schema-Date")) {
                            if (!line.contains("Manifest-Version")) {
                                String dateOfModification = split[2].trim();
                                //If the date of file modification is after the manifest date from the client add file name to manifest token
                                if ((DateUtil.parseStringToDate(dateOfModification, "yyyy-MM-dd")).after(DateUtil.parseStringToDate(dateManifest, "yyyy-MM-dd"))) {
                                    token = token.isEmpty() ? split[0].trim() : token + "," + split[0].trim();
                                }
                            }
                        }
                    }
                }
                input.close();
                reader.close();
                bufferedReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!token.isEmpty()) token = token + ",manifest.txt";
        return token;
    }

    public byte[] getUpdates(String filename) {
        contextPath = applicationProperties.getContextPath();
        byte[] bytes;
        try {
            filename = filename.equalsIgnoreCase("manifest.txt") ?
                    contextPath + "version/" + filename : contextPath + "version/updates/" + filename;
            bytes = FileUtils.readFileToByteArray(new File(filename));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return bytes;
    }

}
