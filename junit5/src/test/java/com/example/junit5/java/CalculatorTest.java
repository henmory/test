package com.example.junit5.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;
import static org.junit.jupiter.api.Assertions.*;

/***
 *
 * Created on：07/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
class CalculatorTest {

    private Calculator calculator;
    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void sum() {
        assumeThat(4.0,is(calculator.sum(2,2)));
    }

    @Test
    void substract() {
        assumeThat(2.0, is(calculator.substract(4,2)));
    }

    @Test
    void divide() {
        assumeThat(2.0, is(calculator.divide(4,2)));
    }

    @Test
    void multiply() {
        assumeThat(2.0, is(calculator.multiply(2,1)));
    }


}