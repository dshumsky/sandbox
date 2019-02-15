package problem;

public class Coins {
    /*
     * A vending machine has the following denominations: 1c, 5c, 10c, 25c, 50c, and $1.
     * Your task is to write a program that will be used in a vending machine to return change.
     * Assume that the vending machine will always want to return the least number of coins or notes.
     * Devise a function getChange(M, P) where M is how much money was inserted into the machine and P the price of the item selected, that returns an array of integers
     * representing the number of each denomination to return.
     * Example:
     * getChange(5, 0.99) should return [1,0,0,0,0,4]
     */
    static String getChange(double M, double P) {
        long cents = Math.round(M * 100) - Math.round(P * 100);
        long c100 = cents / 100;
        long rest = cents - c100 * 100;
        long c50 = rest / 50;
        rest = rest - c50 * 50;
        long c25 = rest / 25;
        rest = rest - c25 * 25;
        long c10 = rest / 10;
        rest = rest - c10 * 10;
        long c5 = rest / 5;
        rest = rest - c5 * 5;
        long c1 = rest;
        return String.format("[%s,%s,%s,%s,%s,%s]", c1, c5, c10, c25, c50, c100);
    }

    public static void main(String[] args) {
        System.out.println(getChange(5, 0.99d));
        System.out.println(getChange(3.14, 1.99));// should return [0,1,1,0,0,1]
        System.out.println(getChange(4, 3.14));// should return [1,0,1,1,1,0]
        System.out.println(getChange(0.45, 0.34));// should return [1,0,1,0,0,0]
    }
}
