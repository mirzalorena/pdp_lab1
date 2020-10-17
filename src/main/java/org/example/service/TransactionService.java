package org.example.service;

import org.example.model.Inventory;
import org.example.model.Product;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransactionService extends Inventory implements Runnable {
    private String transactionName;
    private float totalPrice = 0.0f;
    private Boolean invChanged;
    private Inventory inventory;
    private Lock _mutex = new ReentrantLock();

    public TransactionService(Inventory inventory, String name) {
        this.inventory = inventory;
        this.transactionName = name;
    }

    @Override
    public void run() {
        for (Product product : this.getProducts()){
            _mutex.lock();
            try {
                inventory.remove(product, this.getQuantity(product));
                System.out.println(this.transactionName + ": bought " + product.getName() + " -> " + String.valueOf(this.getQuantity(product)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            _mutex.unlock();
        }
    }

    @Override
    public void add(Product product, int quantity) {
        super.add(product, quantity);
        invChanged = true;
    }

    @Override
    public void remove(Product product, int quantity) {
        super.remove(product, quantity);
        invChanged = true;
    }

    public float getTotalPrice() {
        if (invChanged == null) return 0.0f;
        if (invChanged) {
            this.totalPrice = 0;
            for (Product product : this.getProducts()){
                this.totalPrice += this.getQuantity(product) * product.getUnitPrice();
            }
            this.invChanged = false;
        }

        return this.totalPrice;
    }
}