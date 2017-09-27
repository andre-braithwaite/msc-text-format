/**
 * Created by andre on 10/08/2016.
 */
import java.io.*;
import java.util.*;

public class Format {

    public static void main(String[] args) {

        //String myFile = "french_full_v01_UTF-8.txt";
        String myFile = args[0];
        String outputName = myFile.substring(0, myFile.length() - 4) + "_FORMATTED.csv";

        String line = null;
        Set<String> wordSet = new HashSet<String>();

        try {
            File file = new File(myFile);
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(myFile), "UTF-8"));

            StringBuffer outputStringBuffer = new StringBuffer();
            outputStringBuffer.delete(0, outputStringBuffer.length());

            while ((line = bufferedReader.readLine()) != null) {

                // Remove UTF BOM Characters
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }

                boolean startsWithDigit = Character.isDigit(line.charAt(0));

                if (startsWithDigit) {

                    int spaceIndex = line.indexOf(" ");

                    // Append Rank
                    String rank = line.substring(0, spaceIndex);

                    if (rank.length() == 1) {
                        rank = "000" + rank;
                    } else if (rank.length() == 2) {
                        rank = "00" + rank;
                    } else if (rank.length() == 3) {
                        rank = "0" + rank;
                    }

                    outputStringBuffer.append(rank + ",");

                    // Append French Word
                    outputStringBuffer.append(line.split(" ")[1] + ",");

                    // Append English Definition
                    if (line.split(" ")[2].equals("to")) {

                        String verb = line.split(" ")[3];

                        if (verb.substring(verb.length() - 1).equals(",")) {
                            verb = verb.substring(0, verb.length() - 1);
                        }
                        outputStringBuffer.append(line.split(" ")[2] + " " + verb);
                    } else {
                        String definition = line.split(" ")[2];

                        if (definition.substring(definition.length() - 1).equals(",")) {
                            definition = definition.substring(0, definition.length() - 1);
                        }

                        if (definition.substring(definition.length() - 1).equals(";")) {
                            definition = definition.substring(0, definition.length() - 1);
                        }
                        outputStringBuffer.append(definition);
                    }

                    wordSet.add(outputStringBuffer.toString());
                    outputStringBuffer.delete(0, outputStringBuffer.length());
                }
            }


            fileReader.close();

            List<String> csvRows = new ArrayList<String>(wordSet);

            // Order the words by rank
            Collections.sort(csvRows);

            ListIterator<String> itr = csvRows.listIterator();

            boolean firstIteration = true;
            String previousRank = "";

            while (itr.hasNext()) {

                if (!firstIteration) {
                    previousRank = itr.previous().substring(0,5);
                    itr.next();
                } else {
                    previousRank = "No previous rank";
                }

                boolean extraDef = false;
                extraDef = itr.next().startsWith(previousRank);

                if (extraDef) {
                    itr.remove();
                }
                firstIteration = false;
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputName), "UTF-8"));

            // Write output
            for (String item : csvRows) {
                System.out.println(item);
                writer.write(item);
                writer.newLine();
            }
            writer.close();

            System.out.println();
            System.out.println("ArrayList Size: " + csvRows.size());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e1) {
            e1.printStackTrace();
        }
    }
}