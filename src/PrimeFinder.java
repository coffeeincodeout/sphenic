import java.util.ArrayList;

public class PrimeFinder
{
    /**
     * This function calculates every prime between a minimum number,
     * and a maximum number. Returning a list of the primes
     * @param min Starting value to check if prime
     * @param max Maximum value to check if prime
     * @return list of primes between the min and max value.
     */
    public ArrayList<Long> getPrimeList(long min, long max)
    {
        ArrayList<Long> newList = new ArrayList<>();
        boolean prime = true;

        for(long i = min; i < max; i++)
        {
            for(int j = 2; j < (i / 2); j++)
            {
                if ((i % j) == 0)
                {
                    prime = false;
                    break;
                }
            }
            if(prime) newList.add(i);
            prime = true;
        }
        return newList;
    }
}
