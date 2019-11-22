/**
 *
 * @author aalozie
 */
package org.fhi360.lamis.utility;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.config.ContextProvider;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class FileUtil {
    private final ApplicationProperties properties = ContextProvider.getBean(ApplicationProperties.class);

    public void loadFile(String filepath) throws IOException {
        if (filepath.toLowerCase().endsWith(".zip")) {  //filepath.substring(filepath.lastIndexOf(".")).equals(".zip")
            unzip(filepath);
        } else {
            String directory = new File(filepath).getParent(); //get the pathname as a string
            if (directory != null) {
                File source = new File(directory); //create a file object from the pathname
                File destination = new File(getContextPath() + "exchange");

                //File destination = new File("c:/lamis2/web/exchange");
                if (!destination.exists()) {
                    destination.mkdirs();
                }
                for (File file : source.listFiles()) {  //iterate through the files in the source directory
                    if (file.getName().toLowerCase().endsWith(".xml")) {
                        File f = new File(filepath + "/" + file.getName());
                        f.renameTo(new File(getContextPath() + "exchange/" + file.getName())); //copy to destination directory
                    }
                }
            }
        }
    }

    public String copyInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    public void copyFile(String filename, String source, String destination) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(source + filename));
        FileOutputStream outputStream = new FileOutputStream(new File(destination + filename));
        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }

    public void makeDir(String directory) {
        File destination = new File(directory);
        if (!destination.exists()) {
            destination.mkdirs();
        }
    }

    public void moveFile(String filename, String source, String destination) throws IOException {
        File directory = new File(destination);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File f = new File(source + filename);
        f.renameTo(new File(destination + filename));
    }
    /**
     * A constants for buffer size used to read/write data
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Compresses a list of files to a destination zip file
     *
     * @param listFiles A collection of files and directories
     * @param destZipFile The path of the destination zip file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void zip(List<File> listFiles, String destZipFile) throws FileNotFoundException, IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile));
        for (File file : listFiles) {
            if (file.isDirectory()) {
                zipDirectory(file, file.getName(), zos);
            } else {
                zipFile(file, zos);
            }
        }
        zos.flush();
        zos.close();
    }

    /**
     * Compresses files represented in an array of paths
     *
     * @param files a String array containing file paths
     * @param destZipFile The path of the destination zip file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void zip(String[] files, String destZipFile) throws FileNotFoundException, IOException {
        List<File> listFiles = new ArrayList<File>();
        for (int i = 0; i < files.length; i++) {
            listFiles.add(new File(files[i]));
        }
        zip(listFiles, destZipFile);
    }

    /**
     * Adds a directory to the current zip output stream
     *
     * @param folder the directory to be added
     * @param parentFolder the path of parent directory
     * @param zos the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void zipDirectory(File folder, String parentFolder, ZipOutputStream zos) throws FileNotFoundException, IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                zipDirectory(file, parentFolder + "/" + file.getName(), zos);
                continue;
            }
            zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            long bytesRead = 0;
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = bis.read(bytesIn)) != -1) {
                zos.write(bytesIn, 0, read);
                bytesRead += read;
            }
            zos.closeEntry();
        }
    }
    
    public void zipFolder(String folderName, String destZipFile) throws FileNotFoundException, IOException {
        File folder = new File(folderName);
        List<File> listFiles = new ArrayList<>();
        File[] files = folder.listFiles();
        listFiles.addAll(Arrays.asList(files));
        zip(listFiles, destZipFile);
    }

    /**
     * Adds a file to the current zip output stream
     *
     * @param file the file to be added
     * @param zos the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void zipFile(File file, ZipOutputStream zos) throws FileNotFoundException, IOException {
        zos.putNextEntry(new ZipEntry(file.getName()));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        long bytesRead = 0;
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = bis.read(bytesIn)) != -1) {
            zos.write(bytesIn, 0, read);
            bytesRead += read;
        }
        zos.closeEntry();
    }

    public void unzip(String zipFile) throws IOException {
        File extractTo = new File(getContextPath() + "exchange/");
        //File extractTo = new File("C:/LAMIS2/web/exchange/");
        ZipFile archive = new ZipFile(zipFile);
        Enumeration e = archive.entries();
        while (e.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            File file = new File(extractTo, entry.getName());
            if (entry.isDirectory() && !file.exists()) {
                file.mkdirs();
            } else {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                InputStream in = archive.getInputStream(entry);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                byte[] buffer = new byte[BUFFER_SIZE];
                int readByte;
                while ((readByte = in.read(buffer)) != -1) {  //read and write until last byte is encountered
                    out.write(buffer, 0, readByte);
                }
                in.close();
                out.close();
            }
        }
    }

    public synchronized void deflateFile(String fileName) throws IOException {
        Map<String, Object> map = new PropertyAccessor().getSystemProperties();
        String contextPath = (String) map.get("contextPath");

        //Rename file to be deflated file to temp file
        FileUtils.copyFile(new File(fileName), new File(contextPath + "exchange/deflate.txt"));

        FileInputStream fileInputStream = new FileInputStream(contextPath + "exchange/deflate.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(fileOutputStream);

        //Copy file and compress it
        int oneByte;
        while ((oneByte = fileInputStream.read()) != -1) {
            deflaterOutputStream.write(oneByte);
        }
        deflaterOutputStream.close();
        fileInputStream.close();
        if (new File(contextPath + "exchange/deflate.txt").exists()) {
            new File(contextPath + "exchange/deflate.txt").delete();
        }
    }

    public synchronized void inflateFile(String fileName) throws IOException {
        if (isBinaryFile(fileName)) {
            Map<String, Object> map = new PropertyAccessor().getSystemProperties();
            String contextPath = (String) map.get("contextPath");

            FileInputStream fileInputStream = new FileInputStream(fileName);
            InflaterInputStream inflaterInputStream = new InflaterInputStream(fileInputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(contextPath + "exchange/inflate.txt");

            //Copy deflated file and uncompress it
            int oneByte;
            while ((oneByte = inflaterInputStream.read()) != -1) {
                fileOutputStream.write(oneByte);
            }
            fileOutputStream.close();
            fileInputStream.close();
            FileUtils.copyFile(new File(contextPath + "exchange/inflate.txt"), new File(fileName));
            if (new File(contextPath + "exchange/inflate.txt").exists()) {
                new File(contextPath + "exchange/inflate.txt").delete();
            }

        }
    }

    
    public void inflateFile(String fileName, String facilityId) throws IOException {
        if (isBinaryFile(fileName)) {
            Map<String, Object> map = new PropertyAccessor().getSystemProperties();
            String contextPath = (String) map.get("contextPath");

            FileInputStream fileInputStream = new FileInputStream(fileName);
            InflaterInputStream inflaterInputStream = new InflaterInputStream(fileInputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(contextPath + "exchange/sync/"+facilityId+"/inflate.txt");

            //Copy deflated file and uncompress it
            int oneByte;
            while ((oneByte = inflaterInputStream.read()) != -1) {
                fileOutputStream.write(oneByte);
            }
            fileOutputStream.close();
            fileInputStream.close();
            FileUtils.copyFile(new File(contextPath + "exchange/sync/"+facilityId+"/inflate.txt"), new File(fileName));
            if (new File(contextPath + "exchange/sync/"+facilityId+"/inflate.txt").exists()) {
                new File(contextPath + "exchange/sync/"+facilityId+"/inflate.txt").delete();
            }
        }
    }

    public void deleteFileWithExtension(String sourceFolder, String deleteExtension) {
        FileFilter fileFilter = new FileFilter(deleteExtension);
        File directory = new File(sourceFolder);

        // Put the names of all files ending with .txt in a String array
        String[] listOfTextFiles = directory.list(fileFilter);
        if (listOfTextFiles.length == 0) {
            System.out.println("There are no text files in this directory!");
            return;
        }

        File fileToDelete;
        for (String file : listOfTextFiles) {
            //construct the absolute file paths...
            String absoluteFilePath = new StringBuffer(sourceFolder).append(File.separator).append(file).toString();

            //open the files using the absolute file path, and then delete them...
            fileToDelete = new File(absoluteFilePath);
            boolean isdeleted = fileToDelete.delete();
            System.out.println("File : " + absoluteFilePath + " was deleted : " + isdeleted);
        }
    }

    public void deleteFile(File file) {
        if (file.isDirectory()) {
            //directory is empty, then delete it
            if (file.list().length == 0) {

                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());

            } else {
                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    deleteFile(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }
        } else {
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    public static boolean isBinaryFile(String filename) throws FileNotFoundException, IOException {
        /**
         * Guess whether given file is binary. Just checks for anything under
         * 0x09.
         *
         * If the file consists of the bytes 0x09 (tab), 0x0A (line feed), 0x0C
         * (form feed), 0x0D (carriage return), or 0x20 through 0x7E, then it's
         * probably ASCII text.
         *
         * If the file contains any other ASCII control character, 0x00 through
         * 0x1F excluding the three above, then it's probably binary data.
         */

        InputStream in = new FileInputStream(filename);
        int size = in.available();
        if (size > 1024) {
            size = 1024;
        }
        byte[] data = new byte[size];
        in.read(data);
        in.close();

        int ascii = 0;
        int other = 0;

        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            if (b < 0x09) {
                return true;
            }

            if (b == 0x09 || b == 0x0A || b == 0x0C || b == 0x0D) {
                ascii++;
            } else if (b >= 0x20 && b <= 0x7E) {
                ascii++;
            } else {
                other++;
            }
        }

        if (other == 0) {
            return false;
        }
        return 100 * other / (ascii + other) > 95; //(other / size * 100 > 95); 
    }

    private String getContextPath() {
        return properties.getContextPath();
    }

    public void zipFolderContent(String sourceFolder, String outputZipFile) {
        ZipService zipService = new ZipService();
        zipService.SOURCE_FOLDER = sourceFolder;
        zipService.generateFileList(new File(sourceFolder));
        zipService.zipIt(outputZipFile);
    }

    public class ZipService {

        List<String> fileList;
        private String SOURCE_FOLDER = "source folder";

        ZipService() {
            fileList = new ArrayList<String>();
        }

        /**
         * Traverse a directory and get all files, and add the file into
         * fileList
         *
         * @param node file or directory
         */
        public void generateFileList(File node) {

            //add file only
            if (node.isFile()) {
                fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
            }

            if (node.isDirectory()) {
                String[] subNote = node.list();
                for (String filename : subNote) {
                    generateFileList(new File(node, filename));
                }
            }

        }

        /**
         * Format the file path for zip
         *
         * @param file file path
         * @return Formatted file path
         */
        private String generateZipEntry(String file) {
            System.out.println("Source file:...." + file);
//            return file.substring(SOURCE_FOLDER.length() + 1, file.length());
            return file.substring(SOURCE_FOLDER.length(), file.length()); //use this on the server
        }

        /**
         * Zip it
         *
         * @param zipFile output ZIP file location
         */
        public void zipIt(String zipFile) {

            byte[] buffer = new byte[1024];

            try {
                FileOutputStream fos = new FileOutputStream(zipFile);
                ZipOutputStream zos = new ZipOutputStream(fos);

                System.out.println("Output to Zip : " + zipFile);

                for (String file : this.fileList) {
                    System.out.println("File Added : " + file);
                    ZipEntry ze = new ZipEntry(file);
                    zos.putNextEntry(ze);

                    FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }

                    in.close();
                }

                zos.closeEntry();
                //remember close it
                zos.close();

                System.out.println("Done");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public class FileFilter implements FilenameFilter {

        private String fileExtension;

        public FileFilter(String fileExtension) {
            this.fileExtension = fileExtension;
        }

        @Override
        public boolean accept(File directory, String fileName) {
            return (fileName.endsWith(this.fileExtension));
        }
    }

}
