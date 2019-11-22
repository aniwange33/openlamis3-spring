/**
 * @author user1
 */

package org.fhi360.lamis.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.fhi360.lamis.utility.PropertyAccessor;
import org.springframework.stereotype.Component;

@Component
public class UploadFolderService {

    public void lockUploadFolder(long facilityId) {
        //To lock a folder create a file named lock.ser
        Map<String, Object> map = new PropertyAccessor().getSystemProperties();
        String contextPath = (String) map.get("contextPath");
        String fileName = contextPath + "exchange/sync/" + facilityId + "/lock.ser";
        try {
            File file = new File(fileName);
            FileOutputStream stream = new FileOutputStream(file);
            stream.close();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void unlockUploadFolder(long facilityId) {
        //To unlock a folder delete the file lock.ser
        Map<String, Object> map = new PropertyAccessor().getSystemProperties();
        String contextPath = (String) map.get("contextPath");
        String fileName = contextPath + "exchange/sync/" + facilityId + "/lock.ser";
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getUploadFolderStatus(long facilityId) {
        // If file lock.ser if found in the folder, that folder is locked and cannot be accessed by the client to write data into
        //if the file is not present the folder is unlock and data can be writen into it

        Map<String, Object> map = new PropertyAccessor().getSystemProperties();
        String contextPath = (String) map.get("contextPath");
        String folder = contextPath + "/exchange/sync/" + facilityId + "/";
        if (new File(folder + "lock.ser").exists()) {
            return "locked";
        } else {
            return "unlocked";
        }

    }

    public void lockUploadFolder(String folder) {
        //To lock a folder create a file named lock.ser
        Map<String, Object> map = new PropertyAccessor().getSystemProperties();
        String fileName = folder + "lock.ser";
        try {
            File file = new File(fileName);
            FileOutputStream stream = new FileOutputStream(file);
            stream.close();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void unlockUploadFolder(String folder) {
        //To unlock a folder delete the file lock.ser
        Map<String, Object> map = new PropertyAccessor().getSystemProperties();
        String fileName = folder + "lock.ser";
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getUploadFolderStatus(String folder) {
        // If file lock.ser if found in the folder, that folder is locked and cannot be accessed by the client to write data into
        //if the file is not present the folder is unlock and data can be writen into it

        Map<String, Object> map = new PropertyAccessor().getSystemProperties();
        if (new File(folder + "lock.ser").exists()) {
            return "locked";
        } else {
            return "unlocked";
        }

    }

    public static String getDisableRecordsAll() {
        /*String contextPath = ServletActionContext.getServletContext().getInitParameter("contextPath");
        try {
            File file = new File(contextPath + "exchange/disable.ser");
            if (file.exists()) {
                return "disable";
            } else {
                return "enable";
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
*/
        return null;
    }

    public String getUploadFolderSyncingStatus(long facilityId) {
        // If file lock.ser if found in the folder, that folder is locked and cannot be accessed by the client to write data into
        //if the file is not present the folder is unlock and data can be writen into it

        Map<String, Object> map = new PropertyAccessor().getSystemProperties();
        String contextPath = (String) map.get("contextPath");
        String folder = contextPath + "exchange/sync/" + facilityId + "/";
        if (new File(folder + "process.ser").exists()) {
            return "locked";
        } else {
            return "unlocked";
        }

    }

    public void lockUploadFolderSyncing(String folder) {
        //To lock a folder create a file named lock.ser
        Map<String, Object> map = new PropertyAccessor().getSystemProperties();
        String fileName = folder + "process.ser";
        try {
            File file = new File(fileName);
            FileOutputStream stream = new FileOutputStream(file);
            stream.close();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void unlockUploadFolderSyncing(String folder) {
        //To unlock a folder delete the file lock.ser
        Map<String, Object> map = new PropertyAccessor().getSystemProperties();
        String fileName = folder + "process.ser";
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
