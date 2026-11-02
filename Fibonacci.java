import java.util.Scanner;

public class Fibonacci{
    static int recursiveSteps = 0;


    //Recursive approach
    static int fibo(int n){
        recursiveSteps++;
        if(n == 0 || n == 1){
            return n;
        }
        return fibo(n-1) + fibo(n-2);

    }
    // Non-recursive approach
    static void fibo1(int n) {
        int a=0;
        int b=1;
        int iterativeSteps = 0;
        for(int i=0;i<n;i++){
            System.out.print(a+" ");
            int c=a+b;
            a=b;
            b=c;
            iterativeSteps++;
        }
        System.out.println("\nNon-recursive stepcount: " + iterativeSteps);
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of terms:");
        int n = sc.nextInt();
        System.out.println("Fibonacci series using Recursive approach");
        for(int i=0;i<n;i++){
            System.out.print(fibo(i)+" ");
        }
        System.out.println("Recursive stepcount: "+recursiveSteps);

        System.out.println("Fibonacci series using Non-recursive approach");
        System.out.println("series:");
        fibo1(n);

    }
}