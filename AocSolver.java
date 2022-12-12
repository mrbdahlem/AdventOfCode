/**
 * This interface describes the methods every day's solution MUST implement.
 *
 * @author Brian Dahlem
 * @version 1
 */
public abstract class AocSolution
{
    public abstract void prepare();
    public abstract String part1();
    public abstract String part2();

    // Instantiate and execute a DaySolution class
    protected static void runSolution(Class<?> solutionClass) {

        // Clear the terminal
        System.out.print('\u000C');

        // Weird things happen below.

        // Instantiate the solution from the class's handle. This is a weird
        // way to do this, so you shouldn't try to copy it.
        AocSolution todaysSolution = null;
        try {
            todaysSolution = (AocSolution)solutionClass.getConstructor().newInstance();
        }
        catch (Exception e) {
            // If there is an exception constructing this class, exit with
            // an error message
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Load and parse the input for this day's problem
        todaysSolution.prepare();

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
