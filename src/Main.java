import java.util.ArrayList;
import java.util.Scanner;


public class Main
{
    private static final long MIN_PRIME_NUMBER = 2;
    private static final boolean TERMINATE = true;
    public static ArrayList<Long> primeList = new ArrayList<>(); //List to store all prime numbers
    public static boolean isSphenic = false; //Is a number spheric
    public static boolean notPossibleSphenic = false;

    /**
     * 2698352667883
     * @param args
     */
    public static void main(String[] args)
    {
        long sphenicNumber = Long.parseLong("1028744683451701");
        System.out.println(Long.MAX_VALUE + "\n" + sphenicNumber);
        int threads = 256; //Threads for the spheric number calculation
        int pThreads = 256; //Threads to run prime numbers

        /*
        Scanner input = new Scanner(System.in);
        System.out.print("Sphenic number to find: ");
        sphenicNumber = input.nextLong();
        System.out.print("Prime list threads: ");
        pThreads = input.nextInt();
        System.out.print("Sphenic number threads: ");
        threads = input.nextInt();
        */

        System.out.println("\n\nCalculating prime list between 1 and " + sphenicNumber + " ...");
        long initialTime = System.currentTimeMillis();
        getPrimeList(sphenicNumber, pThreads);
        System.out.println("Time to compute primes with " + pThreads + " threads: " + (System.currentTimeMillis() - initialTime) + "ms");

        unthreadedSphenic(sphenicNumber);
        notPossibleSphenic = false; //Reset variable back.
        threadedSphenic(sphenicNumber, threads);
    }

    /**
     * This function calculates if a number is sphenic unthreaded
     *
     * @param sphenicNumb Sphenic number to check
     */
    public static void unthreadedSphenic(long sphenicNumb)
    {
        long initialTime = System.currentTimeMillis();
        SphenicNumber isSphenic = new SphenicNumber();
        System.out.println("\n\nNumber " + sphenicNumb +" is sphenic?: " + isSphenic.getSphenic(primeList,0,primeList.size(),sphenicNumb));
        System.out.println("Time to compute sphenic unthreaded: " + (System.currentTimeMillis() - initialTime) + "ms");
    }

    /**
     * This function solves if a number is sphenic by dividing the work
     * amongst threads.
     * An array of threads is used to keep track of every thread to know
     * when they have finished.
     * The for loop divides the work by sending in an equal set amount of
     * primes to each thread to calculate and see if three primes multiplied
     * are equal to the sphenic number.
     * Example:
     * Thread 1 will multiply primes 2,3,5,7,11
     * Thread 2 will multiply primes 13,17,19....
     * Each thread receives the prime list and a pointer
     * to the initial value and their maximum value within that list.
     *
     * @param sphenicNumb Sphenic number we are testing
     * @param threads Number of threads
     */
    public static void threadedSphenic(long sphenicNumb, int threads)
    {
        long size = primeList.size() / threads;
        long min, max;
        MultiThreaded threadArr[] = new MultiThreaded[threads];
        long initialTime = System.currentTimeMillis();

        for(int i = 0; i < threads; i++)
        {
            min = i * size;
            if(i + 1 < threads) max = (i + 1) * size;
            else max = primeList.size();

            MultiThreaded newThread = new MultiThreaded();
            newThread.setSphenic(sphenicNumb, min, max);
            newThread.start();
            threadArr[i] = newThread;
        }

        //Wait for all threads to finish
        for(int i = 0; i < threads; i++)
        {
            while (!threadArr[i].getState().equals(Thread.State.TERMINATED) && !isSphenic && !notPossibleSphenic);
            if(isSphenic || notPossibleSphenic)  break;
        }

        System.out.println("\n\nNumber " + sphenicNumb +" is sphenic?: " + isSphenic);
        System.out.println("Time to compute sphenic threaded with " + threads + ": " + (System.currentTimeMillis() - initialTime) + "ms");
        for(int i = 0; i < threads; i++)
        {
            while(!threadArr[i].getState().equals(Thread.State.TERMINATED))
                threadArr[i].setTerminate(TERMINATE);
        }
    }

    /**
     * This function calls upon the prime list threading function.
     * @param prime Maximum number to find.
     * @param threads Amount of threads.
     */
    public static void getPrimeList(long prime, int threads)
    {
            threadedPrime(prime, threads);
    }

    /**
     * Copies each threads list into the global primeList.
     * @param listToCopy List from a thread.
     */
    public static void storeArray(ArrayList<Long> listToCopy)
    {
        primeList.addAll(listToCopy);
    }

    /**
     * This function multi-threads the prime function to split the work.
     * First I created an array of threads to store all the ID's of
     * each thread.
     * *******
     * IMPORTANT POINT:
     * The number of primes is also equal to one fourth the maximum prime
     * because theoretically, multiplying three primes greater than
     * 1 / 4 of the sphenic number will result in a value higher than
     * the spenic number.
     * *******
     * The for loop then divides the amount of numbers each thread will
     * have to solve for primes.
     * Example: All primes between 1  - 10000, using 10 threads
     * Thread 1: Finds all primes between 1 - 1000
     * Thread 2: Finds all primes between 1000 - 2000
     *          ....
     * Thread 3: Finds all primes between 9000-10000
     *
     * When all threads finish,each list is grabbed from each thread,
     * and coppied into the global prime list in this program.
     * @param primeToCheck Maximum prime number
     * @param threads Amount of threads.
     */
    public static void threadedPrime(long primeToCheck, int threads)
    {
        long maxForSphenic = (long) (Math.cbrt(primeToCheck) * 3);
        long size = maxForSphenic / threads;
        final long INITIAL_MAX = -1;
        long min, max;
        long previousMax = INITIAL_MAX;
        MultiThreaded threadArr[] = new MultiThreaded[threads];

        for(int i = 0; i < threads; i++)
        {
            if(i == 0) min = MIN_PRIME_NUMBER;
            else min = previousMax;

            max = size * (i + 1);
            if(i + 1 == threads) max = maxForSphenic;
            if(max < MIN_PRIME_NUMBER) max = maxForSphenic;
            previousMax = max;

            MultiThreaded newThread = new MultiThreaded();
            newThread.setPrimeValues(min, max);
            newThread.start();
            threadArr[i] = newThread;
        }
        //Wait for all threads to finish... Store each threads array in the main array.
        for(int i = 0; i < threads; i++)
        {
            while (!threadArr[i].getState().equals(Thread.State.TERMINATED));
            storeArray(threadArr[i].getNewList());
        }
    }
}
