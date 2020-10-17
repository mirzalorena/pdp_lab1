package org.example;

import org.example.model.Bill;
import org.example.model.Inventory;
import org.example.model.Product;
import org.example.service.TransactionService;
import org.example.util.RandomString;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static Inventory inventory = new Inventory();
    private static List<Product> products = new ArrayList<>();
    private static List<Bill> salesRecords = new ArrayList<>();
    private static List<TransactionService> transactions = new ArrayList<>();

    private static void Write() {
        int i = 0;
        Random random = new Random();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Asus\\OneDrive\\Documents\\facultate\\sem5\\PDP\\lab1\\src\\main\\resources\\products.txt"));
            while ( i < 500 ) {
                //generate random price + quantity for each product
                int quantity = random.nextInt();
                if (quantity < 0)
                    quantity =  (quantity * -1) % 100;
                else
                    quantity %= 100;
                String string = new RandomString().generateRandomString() +  ' ' + random.nextFloat() + ' ' +  quantity + '\n';
                writer.write(string);
                i += 1;
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void Read() {
        File file = new File("C:\\Users\\Asus\\OneDrive\\Documents\\facultate\\sem5\\PDP\\lab1\\src\\main\\resources\\products.txt");
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()){
                Product p = new Product(scanner.next(), scanner.nextFloat());
                products.add(p);
                inventory.add(p, scanner.nextInt());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        //get number of threads from console
        int NUMBER_OF_THREADS;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter number of threads: ");
        String nrOfThreads = in.nextLine();
        NUMBER_OF_THREADS=Integer.parseInt(nrOfThreads);

        //write products in file
        Write();
        //read products from file
        Read();

        float startPoint =  System.nanoTime() / 1000000;

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            //each thread is a new transaction
            TransactionService t = new TransactionService(inventory, "t" + i);

            int product = new Random().nextInt(10);

            for (int j = 0; j < product; j++) {
                int quantity = new Random().nextInt(10);

                int productId = new Random().nextInt(products.size());

                t.add(products.get(productId), quantity);
            }
            transactions.add(t);
        }

        List<Thread> threads = new ArrayList<>();

        transactions.forEach(t -> threads.add(new Thread(t)));

        for (Thread thread : threads){
            thread.start();
        }

        for (Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        verify();

        float endPoint = System.nanoTime() / 1000000;
        System.out.println("\n End work: " + (endPoint - startPoint) / 1000 + " seconds");
    }

    static void verify() {
        System.out.println("Verifying the stock");

        double sum = salesRecords.stream().mapToDouble(i -> i.getProducts().stream().mapToDouble(Product::getUnitPrice).sum()).sum();
        if(transactions.stream().mapToDouble(i ->{
            if (i == null)
                return 0.0f;
            else
                return i.getTotalPrice();
        }).sum() == sum) {
            System.out.println("Failed");
        }
        else {
            System.out.println("Verification Successful!");
        }
    }

}


