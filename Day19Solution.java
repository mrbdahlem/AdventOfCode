import java.util.*;

/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day19Solution extends AocSolver
{
    // Adjust these to test against the proper input file(s)
    public static String SAMPLE_INPUT_FILENAME = "";
//    public static String SAMPLE_INPUT_FILENAME = "sample.txt";
    // sample input files should be plain text files in the
    // input/sample/dayXX folder of this project. If a sample filename
    // is provided, that file's contents will be used when running the
    // solution INSTEAD OF your provided input.

    // Add any member variables you will need for the processed input or
    // to carry between question parts.




    /**
     * Parse the input text for solving part1 and part2.
     */
    public void prepare(String input)
    {
        // process the input and initialise instance variables


    }


    /**
     * Solve part 1 of this day's problem.
     * @return the solution to part 1 of the problem
     */
    public String part1()
    {
        String solution = "";
        // Solve part 1 for this day here


        return solution;
    }

    /**
     * Solve part 2 of this day's problem.
     * @return the solution to part 2 of the problem
     */
    public String part2()
    {
        String solution = "";
        // Solve part 2 for this day here


        return solution;
    }

    /**
     * A runner method for this day's solution.
     * In BlueJ, pass in a sample file name like {"sample.txt"} if the file
     * is stored in input/sample/dayXX/.
     */
    public static void main(String[] args) {
        // Get a handle to this class (so that each day's solution follows
        // the same template
        Class<?> thisClass = java.lang.invoke.MethodHandles.lookup().lookupClass();
        AocSolver.runSolution(thisClass, args, SAMPLE_INPUT_FILENAME);
    }
}
