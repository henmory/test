package com.example.junit5.java;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
class PortfolioTest {
    Portfolio portfolio;
    StockService stockService;
    PortfolioTest tester;
    @BeforeEach
    void setUp() {

        tester = new PortfolioTest();

        //Create a portfolio object which is to be tested
        portfolio = new Portfolio();

        //Create the mock object of stock service 创建一个模拟对象
        stockService = mock(StockService.class);

        //set the stockService to the portfolio
        portfolio.setStockService(stockService);
    }

    @Test
    void getMarketValue() {

        //Creates a list of stocks to be added to the portfolio
        List<Stock> stocks = new ArrayList<Stock>();
        Stock googleStock = new Stock("1","Google", 10);
        Stock microsoftStock = new Stock("2","Microsoft",100);

        stocks.add(googleStock);
        stocks.add(microsoftStock);

        //add stocks to the portfolio
        portfolio.setStocks(stocks);

        //mock the behavior of stock service to return the value of various stocks 生命模拟对象的行为以及它的凡或址
        when(stockService.getPrice(googleStock)).thenReturn(50.00);
        when(stockService.getPrice(microsoftStock)).thenReturn(1000.00);

        double marketValue = portfolio.getMarketValue();
        assumeThat(100500.0, equalTo(marketValue));
    }
}