import java.util.ArrayList;

public class MultiThreaded extends Thread
{
    private boolean sphenic;
    private long min, max, spehenicVal;
    private ArrayList<Long> newList;
    private boolean terminate = false;
    private SphenicNumber newSphenic;

    /**
     * This runs every time a new thread is created. We use this class
     * for both the sphenic and the primelist.
     * if this thread is for the sphenic calculation, the sphenic variable
     * will be true.
     * Sphenic = true
     *      Creates a new sphenic calculating thread. The SphenicNumber class
     *      is called and passed the specific amount of numbers it has to
     *      calculate. If at any point, any thread returns that the value
     *      we are checking is a sphenic number, a global variable
     *      is set to true "Main.isSphenic" terminating all other threads.
     *
     * Sphenic = false
     *      It calculates the primelist. Each thread calculates a set amount
     *      of primes. Between the min and max values passed in.
     */
    public void run()
    {
        if(!terminate && !Main.isSphenic)
        {
            if (!sphenic)
            {
                PrimeFinder newPrime = new PrimeFinder();
                newList = newPrime.getPrimeList(min, max);
            }
            else
            {
                newSphenic = new SphenicNumber();
                if(newSphenic.getSphenic(Main.primeList, min, max, spehenicVal))
                {
                    Main.isSphenic = true;
                }
            }
        }
    }

    /**
     * This function is called from the main class to set the minimum
     * and maximum values for this thread to find primes/
     * @param min Minimum value
     * @param max Maximum value
     */
    public void setPrimeValues(long min, long max)
    {
        this.min = min;
        this.max = max;
        sphenic = false;
    }

    /**
     * This function is called from the main class to set the minimum,
     * maximum values. It also sets the sphenic value we are testing for.
     * @param sphenicVal Sphenic value to test for
     * @param min Minimum value
     * @param max Maximum value
     */
    public void setSphenic(long sphenicVal, long min, long max)
    {
        this.min = min;
        this.max = max;
        this.spehenicVal = sphenicVal;
        sphenic = true;
    }

    /**
     * This is used by the main class to grab the prime list within
     * each specific thread.
     * @return Array list of long values (primes).
     */
    public ArrayList<Long> getNewList()
    {
        return newList;
    }

    /**
     * Terminate this thread (and the sphenic algorithm).
     * @param term True to terminate.
     */
    public void setTerminate(boolean term)
    {
        terminate = term;
        if(newSphenic != null) newSphenic.setTerminate(term);
    }

}
