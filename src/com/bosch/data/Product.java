package com.bosch.data;

public class Product {

    private Integer id;
    private String pcb;

    public Product(Integer id, String pcb) {
        this.id = id;
        this.pcb = pcb;
    }

    public Integer getId() {
        return id;
    }

    public String getPcb() {
        return pcb;
    }

}
