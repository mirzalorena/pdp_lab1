package org.example;


//class MultithreadingDemo extends Thread
class MultithreadingDemo implements Runnable
{
    public void run()
    {
        try
        {
            // Displaying the thread that is running
            System.out.println ("Thread " +
                    Thread.currentThread().getId() +
                    " is running");

        }
        catch (Exception e)
        {
            System.out.println ("Exception is caught");
        }
    }
}

// Main Class
public class ThreadTest
{
        public static void main(String[] args)
        {
            int n = 10; // Number of threads
            for (int i=0; i<n; i++)
            {
                Thread object = new Thread(new MultithreadingDemo());
                object.start();
            }
        }

}
