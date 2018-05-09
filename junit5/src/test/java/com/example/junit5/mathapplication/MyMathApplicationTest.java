package com.example.junit5.mathapplication;

import com.example.junit5.service.IComputer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assume.assumeThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/***
 *
 * Created on：09/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
class MyMathApplicationTest {

    MyMathApplication mathApplication;
    IComputer iComputer;
    @BeforeEach
    void setUp() {

        iComputer = mock(IComputer.class);

        mathApplication = new MyMathApplication(iComputer);
    }

    @Test
    void add() {
        when(iComputer.add(1,2)).thenReturn(3);
        assumeThat(3, equalTo(mathApplication.add(1,2)));
    }

    @Test
    void subtract() {
        when(iComputer.subtract(1,2)).thenReturn(3);
        assumeThat(3, equalTo(mathApplication.subtract(1,2)));}

    @Test
    void multiply() {
        when(iComputer.multiply(1,2)).thenReturn(3);
        assumeThat(3, equalTo(mathApplication.multiply(1,2)));}

    @Test
    void divide() {
        when(iComputer.divide(1,2)).thenReturn(3);
        assumeThat(3, equalTo(mathApplication.divide(1,2)));}
}