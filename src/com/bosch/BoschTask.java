package com.bosch;

import com.bosch.service.DataService;

public class BoschTask {

    public static void main(String[] args) {
        final DataService dataService =  DataService.getInstance();

        dataService.loadDataFromDB();
        dataService.createProductionList();
        dataService.saveProductionListToFile();
        dataService.readProductionListFromFile();
        dataService.saveProductionListToTable();
    }
}
