/**
 * @author John Paul Smith
 *
 *A simple stand-alone program to demonstrate the basics of prime decomposition:
 *input a positive integer and receive that integer's prime constituents. This 
 *general problem is a key component of public key cryptography. 
 *
 *I originally create this program to solve the third Project Euler challenge 
 *problem: http://projecteuler.net/problem=3.
 *
 *The algorithm to determine the prime factors is a tuned trial division
 *implementation, with a maximum input value equal to the maximum positive
 *value of a 64-bit integer in the JVM. 
 * 
 *In expectation, this program probably won't take much longer than 90 seconds 
 *to factor the input number, because most numbers are comprised of several to
 *many smaller prime factors. However, a number that is the square of a 
 *ten-digit prime or the product of two ten-digit primes is the worst-case for 
 *this program and the general problem of integer factorization as a whole. To
 *see this, choose 9223371873002223329 as the input. As the only factors are 
 *3037000493 and 3037000453, the algorithm will take a very long time to find 
 *them, likely between six and eight hours on a relatively fast personal 
 *computer. 
 *
 *Recommended test inputs: 7653567865434567, 35184372088832, and 600851475143.
 */

package integerfactorizationapp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class IntegerFactorizationApp {

    static final long N_LIMIT = Long.MAX_VALUE;
    JFrame mainAppFrame;

    public IntegerFactorizationApp() {

        mainAppFrame = new JFrame("Integer Factorization");
        mainAppFrame.getContentPane().add(new MainAppPanel());

        mainAppFrame.setResizable(false);
        mainAppFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainAppFrame.pack();
    }

    /**
     * @param N The positive integer value to find the prime factors for.
     * 
     * @return A String representation of the factors of N, or a message stating that N is a prime number.  
     */
    static String factorInt(final long N) {

        long startTime = System.currentTimeMillis(), duration = 0L;

        ArrayList<Long> factors = getFactors(N);

        if (factors.size() < 2) {
            return NumberFormat.getInstance().format(N) + " is a prime number -- try again";
        }

        duration = System.currentTimeMillis() - startTime;

        String output = "The prime factors of " + NumberFormat.getInstance().format(N) + " are: \n\n";

        for (int i = 0; i < factors.size(); ++i) {

            long x = factors.get(i);

            output += NumberFormat.getInstance().format(x);

            /*
             * Find the number of occurrences of each factor, then convert this value to a string and 
             * replace its regular ASCII digit characters with their corresponding UNICODE superscript 
             * characters using the UNICODE values for each.
             *
             * The 1, 2, 3 superscript characters are ISO 8859-1 ("Latin 1).
             */
            
            int frequency = Collections.frequency(factors, x);
            
            if (frequency > 1) {

                String s = Integer.toString(frequency);
                
                s = s.replaceAll("0", Character.toString('\u2070'));
                s = s.replaceAll("1", Character.toString('\u00B9'));
                s = s.replaceAll("2", Character.toString('\u00B2'));
                s = s.replaceAll("3", Character.toString('\u00B3'));
                s = s.replaceAll("4", Character.toString('\u2074'));
                s = s.replaceAll("5", Character.toString('\u2075'));
                s = s.replaceAll("6", Character.toString('\u2076'));
                s = s.replaceAll("7", Character.toString('\u2077'));
                s = s.replaceAll("8", Character.toString('\u2078'));
                s = s.replaceAll("9", Character.toString('\u2079'));

                output += s;

                i = factors.lastIndexOf(x);
            }

            if (i < factors.size() - 1) {
                output += " * ";
            }
        }

        output += "\n\nCalculated in approx. " + NumberFormat.getInstance().format(duration) + " ms";

        return output;        
    }

    /**
     * @param n The integer value to be tested for primality. Any value less than 2 always returns false.
     * 
     * @return a boolean indicating whether or not n is a prime number.  
     */
    static boolean isPrime(long n) {
        
        if ((n != 2 && n % 2 == 0) || n < 2) {
            return false;
        }

        long sqrtOf = (long) Math.sqrt(n);

        for (long i = 3; i <= sqrtOf; i += 2) {

            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * 
     * @param N The positive integer to be factored into its prime constituents. A negative value will result in a size 0
     * list being returned. A size 0 list should not in and of itself be used to verify primality; a negative value is not 
     * prime.
     * 
     * @return An ArrayList<Long> object containing the prime factors of N, if any.
     */
    static ArrayList<Long> getFactors(final long N) {
        
        ArrayList<Long> factors = new ArrayList<>();
        
        if (N < 2) {
            return factors;
        }
        
        int x = 1000, i, c = 1;
        long b = 1L, n = N;
        long[] primes;
        double s = Math.sqrt(N);
        
        while (b <= s) {
            
            primes = getNextXPrimes(b, x);
            
            i = 0;
            
            while (n > 1 && i < primes.length) {
                
                if (n % primes[i] == 0) {
                    
                    factors.add(primes[i]);
                    
                    n /= primes[i];
                    
                } else {
                    ++i;
                }
            }
            
            b = primes[primes.length - 1];
            
            if (c++ % 100 == 0) {                
                
                if (sumFactors(factors) == N) {
                    return factors;
                }
            }
        }
        
        /* 
         * An integer n can have at most one prime factor that is greater than the floor of its square root. 
         * To find it, divide n by t where t is the product of all prime factors less that the square root
         * of n. If t != n, then n / t is the remaining factor of n.
         */         
        
        long t = sumFactors(factors);
        
        if (t != N && (N % t) == 0) {
            factors.add(N / t);
        }      
        
        return factors;
    }
    
    static long sumFactors(ArrayList<Long> a) {
        
        long sum = 1L;
        
        for (long f : a) {
            sum *= f;
        }
        
        return sum;
    }
    
    /**
     * Finds the next x primes greater than b. 
     * 
     * @param b Incrementally test all values greater than b for primality until x primes have been found.
     * @param x The number of primes greater than b to find. 
     * @return A long[] containing the next x primes greater than b.
     */
    static long[] getNextXPrimes(long b, int x) {       
        
        long[] factors = new long[x];
        
        int i = 0;
        
        while (i < x) {            
            if (isPrime(++b)) {
                factors[i++] = b;
            }
        }
        
        return factors;
    }

    public static void main(String[] args) {
        new IntegerFactorizationApp().mainAppFrame.setVisible(true);               
    }
}

class MainAppPanel extends JPanel implements ActionListener {

    JPanel inputPanel, displayPanel;
    JTextField inputField;
    JTextArea answerField;
    NumberFormat format;
    final Color panelColor = new Color(190, 210, 210);
    final int PANEL_WIDTH = 768;

    MainAppPanel() {

        inputPanel = new JPanel();
        displayPanel = new JPanel();
        displayPanel = new JPanel();
        inputField = new JTextField(20);
        format = NumberFormat.getInstance();
        answerField = new JTextArea();

        answerField.setBackground(panelColor);
        answerField.setFont(new Font("Times", Font.BOLD, 18));
        answerField.setEditable(false);

        inputPanel.setPreferredSize(new Dimension(PANEL_WIDTH, 40));
        inputPanel.setBackground(panelColor);
        inputPanel.add(new JLabel("Enter integer to factor: "));
        inputPanel.add(inputField);

        displayPanel.setBackground(panelColor);
        displayPanel.setPreferredSize(new Dimension(PANEL_WIDTH, 256));
        displayPanel.add(answerField);

        inputField.setBackground(new Color(225, 225, 225));
        inputField.addActionListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(inputPanel);
        add(displayPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        /**
         * The following code allows only one instance of AnswerWriter to run at the same time. 
         * If no other instances are currently executing, a new instance will be started.
         */
        Thread c = Thread.currentThread();

        Thread[] t = new Thread[c.getThreadGroup().activeCount()];

        c.getThreadGroup().enumerate(t);

        for (Thread x : t) {

            if (x instanceof Factorer) {

                answerField.setText("Waiting on current execution -- please wait");

                return;
            }
        }

        new Factorer().start();
    }

    /**
     * The actual factoring is done in a separate thread. Sufficiently large values of n will require 
     * significant time to factor, and since the .actionPerformed method will not return until all the methods 
     * above  it on the stack in the same thread finish, calling the .factorInt method from the .actionPerformed 
     * method will cause the program to lock until .factorInt returns, which includes not being able to close 
     * the JFrame by clicking on the 'x'. This can be a nuisance and will require the manual cancellation of 
     * the program from the IDE or command line. Performing the factoring in a separate thread allows the program 
     * to be closed from the JFrame even in the middle of factoring. The private inner class Factorer handles this.
     * This class also handles the application's input validation.
     */
    private class Factorer extends Thread {

        @Override
        public void run() {

            String input = inputField.getText();

            inputField.setText("");
            answerField.setText("");

            if (input.matches("[0-9]+?")) {

                try {                    
                    long n = Long.parseLong(input);

                    if (n >= IntegerFactorizationApp.N_LIMIT || n < 2) {

                        answerField.setText("Please enter a number between 2 and " + format.format(IntegerFactorizationApp.N_LIMIT));

                    } else {
                        answerField.setText(IntegerFactorizationApp.factorInt(n));
                    }
                } catch (NumberFormatException ex) {/*input numbers that are larger than Long.MAX_VALUE will cause this to be thrown*/

                    answerField.setText("Please enter a number between 2 and " + format.format(IntegerFactorizationApp.N_LIMIT));
                }
            } else {
                answerField.setText("Invalid entry -- try again");
            }
        }
    }
}
