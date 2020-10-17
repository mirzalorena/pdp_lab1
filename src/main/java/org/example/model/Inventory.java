package org.example.model;

import java.util.HashMap;
import java.util.Set;

public class Inventory {
    private HashMap<Product, Integer> products;

    public Inventory() {
        this.products = new HashMap<>();
    }

    public Set<Product> getProducts(){
        return this.products.keySet();
    }

    public int getQuantity(Product product) {
        return this.products.getOrDefault(product, 0);
    }

    public void add(Product product, int quantity){
        if (this.products.containsKey(product)){
            this.products.replace(product, this.products.get(product) + quantity);
        }else{
            this.products.put(product, quantity);
        }
    }

    public void remove(Product product, int quantity){
        if (this.products.containsKey(product)){
            if (this.getQuantity(product) < quantity){
                throw new RuntimeException("Not enough");
            }
            this.products.replace(product, this.products.get(product) - quantity);
            if (this.getQuantity(product) == 0){
                this.products.remove(product);
            }
        }else{
            throw new RuntimeException("Does not exist");
        }
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "products=" + products +
                '}';
    }
}
