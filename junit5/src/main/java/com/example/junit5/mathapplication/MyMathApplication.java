package com.example.junit5.mathapplication;

import com.example.junit5.service.IComputer;

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
public class MyMathApplication {
    private IComputer iComputer;

    public MyMathApplication(IComputer iComputer) {
        this.iComputer = iComputer;
    }

    public int add(int a, int b){
        return iComputer.add(a, b);
    }
    public int subtract(int a, int b){
        return iComputer.subtract(a, b);
    }
    public int multiply(int a, int b){
        return iComputer.multiply(a, b);
    }
    public int divide(int a, int b){
        return iComputer.divide(a, b);
    }

}
