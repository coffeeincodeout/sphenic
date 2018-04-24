import java.util.ArrayList;

public class SphenicNumber
{
    private boolean terminate = false;

    /**
     * multiply a * b * c. Every loop calculates each number.
     * The algorithm begins at the minimum position (divided amongst threads)
     * and iterates through the entire array
     * ****
     * The reason why you have to iterate the entire array is because
     * the prime number can be in a different memory space of another thread
     * If that occurs, the algorithm will never know if the number is sphenic or not.
     * ****
     * A way to make the "false" of the sphenic work efficiently while threaded, is
     * to first store if the current's thread minimum value multiplied by itself
     * three times is less than the value we are searching.
     * If at any point the algorithm obtains a bigger number than the value
     * we are testing, we know the value we are testing cannot be a sphenic number
     *
     * Finally the algorithm returns true or false whether a sphenic number was found.
     *
     * @param primeList List of prime numbers
     * @param min starting position in the prime list for this specific thread to calculate
     * @param max last position in the prime list for this specific thread to calculated
     * @param value Sphenic value we are testing.
     * @return True if a sphenic number is found.
     */
    public boolean getSphenic(ArrayList<Long> primeList, long min, long max, long value)
    {
        long a, b, c;
        boolean minFound = primeList.get((int) min) * primeList.get((int) min) * primeList.get((int) min) < value;

        for(long i = min; i < max; i++)
        {
            a = primeList.get((int) i);
            if(a * a * a > value)
            {
                //Impossible to get a sphenic number
                if(minFound) Main.notPossibleSphenic = true;
                return false;
            }
            for(long j = i + 1; j < primeList.size() - 1; j++)
            {
                b = primeList.get((int) j);
                if(a * b * b > value) break;
                for(long k = j + 1; k < primeList.size() - 2; k++)
                {
                    c = primeList.get((int) k);

                    if(terminate) return true;
                    else if((a * b * c) > value) break;
                    else if((a * b * c) == value) return true;
                }
            }
        }
        return false;
    }

    /**
     * Function to initialize the termination of this algorithm.
     * @param term True to terminate this algorithm
     */
    public void setTerminate(boolean term)
    {
        this.terminate = term;
    }
}
