import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by hakyoshyt on 4/12/18.
 */
public class PrimeNumber implements Runnable{

    private static int primeNumber;
    private static Scanner sc = new Scanner(System.in);
    private static List<Integer> primeNumbers;



    public PrimeNumber (int number){

        this.primeNumber = number;

    }

    public static int getPrimeNumber() {
        return primeNumber;
    }

    public static List<Integer> primeNumbers() {
        primeNumbers = new LinkedList<>();
        if (getPrimeNumber() >= 2) {
            primeNumbers.add(2);
        }
        for (int i = 3; i <= getPrimeNumber(); i += 2) {
            if (isPrime(i)) {
                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }

    private static boolean isPrime(int number) {
        for (int i = 2; i*i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void run() {

        try{

            primeNumbers();
            //for(int i = 0; i < primeNumbers().size(); i++)
                //System.out.println(primeNumbers().get(i));

        }
        catch(Exception e){

            System.out.println(e);
        }



    }

    public static void main(String[] args){

        long startTime = System.currentTimeMillis();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a prime number");

        int x1 = sc.nextInt();

        PrimeNumber p1 = new PrimeNumber(x1);

        Thread t1 = new Thread(p1);

        t1.start();


        long endTime = System.currentTimeMillis();
        System.out.println("Took "+(endTime - startTime) + " ms");

    }


}
