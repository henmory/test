package com.example.junit5.java;

import java.util.List;

/***
 *
 * Created on：08/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class Portfolio {

    private StockService stockService;
    private List<Stock> stocks;

    public StockService getStockService() {
        return stockService;
    }

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public double getMarketValue(){
        double marketValue = 0.0;

        for(Stock stock:stocks){
            marketValue += stockService.getPrice(stock) * stock.getQuantity();
        }
        return marketValue;
    }
}
