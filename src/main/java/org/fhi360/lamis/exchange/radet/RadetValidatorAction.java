/**
 * @author AALOZIE
 */
package org.fhi360.lamis.exchange.radet;

import java.io.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.config.ApplicationProperties;

public class RadetValidatorAction {

    private HttpServletRequest request;
    private HttpSession session;
    private ArrayList<Map<String, String>> validationList = new ArrayList<Map<String, String>>();
    private final ApplicationProperties applicationProperties = ContextProvider.getBean(ApplicationProperties.class);

    public String analyze() {
        System.out.println("...in analyze");

        int active = 0;
        int validated = 0;
        int refillPeriodOver = 0;
        String errors = "";
        String facility = "";
        String period = "";

        try {
            for (int i = 1; ; i++) {
                File filePerm = new File("c:/radet/radet" + i + ".csv");
                if (!filePerm.exists()) {
                    break;
                }

                File radet = new File("radet");

                InputStream in = new FileInputStream(filePerm);
                OutputStream out = new FileOutputStream(radet);
                IOUtils.copy(in, out);    //attachment.renameTo(archive);

                filePerm = null;
                in.close();
                out.close();

                InputStream is = new FileInputStream(radet);
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(reader);

                String row = "";
                int cnt = 0;
                while ((row = br.readLine()) != null) {

                    cnt++;
                    if (cnt < 11) {
                        continue; // Not needed rows
                    }
                    String[] values = row.split(",");

                    if (values[2].equalsIgnoreCase("FHI 360") && values[9].equals("")) {
                        break;
                    }

                    //String facilityCode = values[0] == "" ? null : values[0];
                    System.out.print("  " + values[1]);
                    System.out.print("  " + values[3]);
                    System.out.print("  " + values[4]);
                    System.out.print("  " + values[5]);
                    System.out.print("  " + values[6]);
                    System.out.print("  " + values[7]);
                    System.out.print("  " + values[9]);
                    System.out.print("  " + values[10]);
                    System.out.print("  " + values[11]);
                    System.out.print("  " + values[12]);
                    System.out.print("  " + values[13]);
                    System.out.print("  " + values[14]);
                    System.out.print("  " + values[15]);
                    System.out.print("  " + values[16]);
                    System.out.println("  " + values[17]);

                    facility = values[3];

                    if (values[17].equalsIgnoreCase("Active")) {
                        active++;


                        if (!values[11].equals("")) {
                            if (values[11].indexOf("-") != -1) {
                                period = values[11].substring(0, values[11].indexOf("-"));
                            } else {
                                period = values[11];
                                if (period.trim().equals("")) {
                                    if (!values[9].equals("")) {
                                        errors += values[9].substring(values[9].lastIndexOf("-") + 1) + "-Line" + values[1] + ", ";
                                    }
                                    continue;
                                }
                            }
                            if (Integer.parseInt(period) > 3) {
                                refillPeriodOver++;
                            }
                        }

                        if (values[10].equals("")) {
                            if (!values[9].equals("")) {
                                errors += values[9].substring(values[9].lastIndexOf("-") + 1) + "-Line" + values[1] + ", ";
                            }
                            continue;
                        }

                        Date dateLastRefill = parseDate(values[10], new SimpleDateFormat("dd-MMM-yyyy"));
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateLastRefill);

                        Calendar cal2 = Calendar.getInstance();
                        cal2.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);

                        cal2.add(Calendar.DATE, -1);
                        cal2.add(Calendar.MONTH, Integer.parseInt(period) + 3);

                        Calendar cal3 = Calendar.getInstance();
                        cal3.setTime(new Date());

                        Calendar cal4 = Calendar.getInstance();
                        cal4.set(cal3.get(Calendar.YEAR), cal3.get(Calendar.MONTH), 1);

                        if (cal4.compareTo(cal2) <= 0) {
                            validated++;
                        } else {
                            if (!values[9].equals("")) {
                                errors += values[9].substring(values[9].lastIndexOf("-") + 1) + "-Line" + values[1] + ", ";
                            }
                        }
                    }
                }

                is.close();
                reader.close();
                br.close();

            }

            System.out.println();
            System.out.println(facility);
            System.out.println("Active - " + active);
            System.out.println("Validated - " + validated);
            System.out.println("Refill Periods Over 3 Months - " + refillPeriodOver);
            System.out.println("Lines With Errors - " + errors);

            Map<String, String> map = new HashMap<String, String>();
            map.put("facility", facility);
            map.put("errors", errors);
            map.put("active", Integer.toString(active));
            map.put("validated", Integer.toString(validated));
            map.put("refillPeriodOver", Integer.toString(refillPeriodOver));
            validationList.add(map);
        } catch (Exception e) {
            System.out.println("...Error reading csv file " + e.getMessage());
            throw new RuntimeException(e);
        }

        return "SUCCESS";
    }

    public static Date parseDate(String dateString, DateFormat dateFormat) {
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return date;
    }

//aFileCount = ADIR(aFileInfor, '&mFileType') &&& Get the names of all Passport images 

    /**
     * @return the ancList
     */
    public ArrayList<Map<String, String>> getValidationList() {
        return validationList;
    }


    public void setValidationList(ArrayList<Map<String, String>> validationList) {
        this.validationList = validationList;
    }
}
