import java.nio.file.*;
import java.io.*;
import java.util.*;

import java.time.LocalDate;
import java.net.*;
import javax.swing.JOptionPane;

/**
 * This interface describes the methods every day's solution MUST implement.
 *
 * @author Brian Dahlem
 * @version 1
 */
public abstract class AocSolver
{
    public abstract void prepare(String input);
    public abstract String part1();
    public abstract String part2();

    // Instantiate and execute a DaySolution class
    protected static void runSolution(Class<?> solutionClass, String[] args, String sampleName) {
        if (args.length > 0 &&
                (sampleName == null || sampleName.isEmpty())) {
            sampleName = args[0];
        }

        // Clear the terminal
        System.out.print('\u000C');

        // Determine the day number from the name of the class
        int day = Integer.parseInt(solutionClass.getSimpleName().replaceAll("\\D", ""));

        System.out.println("\u000CAdvent of Code day " + day + " solution:");

        // Load the day's input file, or a sample file if provided
        String input = Helper.loadInput(day, sampleName);

        // Weird things happen below.

        // Instantiate the solution from the class's handle. This is a weird
        // way to do this, so you shouldn't try to copy it.
        AocSolver todaysSolution = null;
        try {
            todaysSolution = (AocSolver)solutionClass.getConstructor().newInstance();
        }
        catch (Exception e) {
            // If there is an exception constructing this class, exit with
            // an error message
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Load and parse the input for this day's problem
        todaysSolution.prepare(input);

        // Solve both parts of the problem
        String part1Solution = todaysSolution.part1();
        String part2Solution = todaysSolution.part2();

        // Print out the solution for both parts. This happens at the end
        // so that answers can be copied/pasted even if debugging output is
        // provided
        System.out.println("Part 1 Solution:");
        for (String line : part1Solution.split("\n")) {
            System.out.println(">" + line + "<");
        }

        System.out.println("Part 2 Solution:");
        for (String line : part2Solution.split("\n")) {
            System.out.println(">" + line + "<");
        }
    }
}


/**
 * A class to help Advent of Code solutions to download their input files
 */
class Helper {
    public static String loadInput(int day, String SampleInputFilename) {

        String input = "";

        // If a sample input file has been provided,
        // run against the sample data
        if (SampleInputFilename != null &&
                !SampleInputFilename.isEmpty()) {
            try{
                String filename = String.format("input/sample/day%02d/%s",
                        day, SampleInputFilename);
                input = fileContents(filename);
            }
            catch (IOException e) {
                System.err.println(e.getLocalizedMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }
        else {
            // otherwise, load the day's input
            input = getInput(day);
        }

        return input;
    }

    /**
     * A method to load everything from a file into a String
     * @param filename the name (and path) of the file to load
     * @return the contents of the file
     */
    private static String fileContents(String filename)
            throws IOException {

        File file = new File(filename);

        return Files.readString(file.toPath());
    }

    /**
     * Automatically download problem input if the input.txt file doesn't
     * exist
     * @param day the day (1-25) of input to download
     * @return the input data
     */
    private static String getInput(int day) {
        int year;

        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("settings.txt")) {
            props.load(in);
        }
        catch (IOException e) {
            // there is no settings.txt property file, so try to create one
            props.setProperty("YEAR", getYear());
            props.setProperty("SESSION", getSessionCookie());

            // try to save the settings.txt file
            try (FileOutputStream out = new FileOutputStream("settings.txt")) {
                props.store(out, "");
            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "Could not save Advent of Code year/session to settings.txt: " +
                                ex.getLocalizedMessage(),
                        "Could not write file",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        String yearString = props.getProperty("YEAR",
                "" + LocalDate.now().getYear());
        year = Integer.parseInt(yearString);

        // Use a default filename for the day's input
        String filename = String.format("input/day%02d.txt", day);

        // Attempt to load the input file if it is already downloaded
        File inputFile = new File(filename);
        if (inputFile.exists()) {
            System.out.println("Using cached input from " + filename);
            try {
                return fileContents(filename);
            }
            catch (IOException e) {
                System.err.println(e.getLocalizedMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }

        // make sure a session cookie has been provided for downloading
        // authorization
        String cookie = props.getProperty("SESSION");

        if (cookie == null || cookie.length() < 90) {
            cookie = getSessionCookie();
        }

        if (cookie == null || cookie.length() < 90) {
            System.err.println("You need a session cookie to download input.");
            throw new RuntimeException("You need a session cookie to download input.");
        }

        String data = ""; // The downloaded file data

        try {
            // Create parent directories as necessary for input file
            inputFile.getParentFile().mkdirs();

            System.out.println("Downloading input data for " + year +
                    " day " + day);
            URL url = new URL("https://adventofcode.com/" + year +
                    "/day/" + day + "/input");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", "session=" + cookie);

            con.setInstanceFollowRedirects(true);
            int status = con.getResponseCode();

            if (status > 299) {
                System.err.println("Error downloading input:");
                System.err.println("Connection status " + status +
                        " downloading input for " + year + " day " + day);
                Reader streamReader = new InputStreamReader(con.getErrorStream());
                BufferedReader in = new BufferedReader(streamReader);
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.err.println(inputLine);
                }
                System.err.println("You may want to delete settings.txt");
                System.exit(0);
            }

            // Streams to handle input from http connection, output to file
            InputStream is = con.getInputStream();
            Reader streamReader = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(streamReader);
            FileOutputStream fos = new FileOutputStream(inputFile);
            PrintStream ps = new PrintStream(fos);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                data += inputLine + "\n";
                ps.print(inputLine + "\n");
            }

            fos.close();
            is.close();
            System.out.println("Input data saved to " + filename);
        }
        catch (Exception e) {
            System.err.println(e);
            System.exit(0);
        }

        return data;
    }

    private static String getYear() {
        int year = LocalDate.now().getYear();
        String yearString = JOptionPane.showInputDialog("Which Advent of Code year are you completing?", year);
        if (yearString == null) {
            yearString = "" + year;
        }

        return yearString;
    }

    private static String getSessionCookie() {
        String message = "In order to download the input files, you will need your session cookie\n" +
                "which allows access in your name to the Advent of Code site. To get this\n" +
                "cookie, you will need to log into the Advent of Code website using a web\n" +
                "browser. Using Chrome, you can go to https://adventofcode.com and press\n" +
                "F12 to display the Developer Console. At the top of the console, is a\n" +
                "menu with `Elements`, `Console`, `Sources`, etc.  You want the\n" +
                "`Application` tab, you might need to press the `>>` button to find it.\n" +
                "On the Application page, look for Cookies under the Storage item. Click\n" +
                "`Cookies`, then click `https://adventofcode.com`. Find the cookie named\n" +
                "`session`. The Value of this cookie is what you want. Double click the\n" +
                "value and press Ctrl-C to copy the value, then paste it here.";

        return JOptionPane.showInputDialog(message);
    }
}