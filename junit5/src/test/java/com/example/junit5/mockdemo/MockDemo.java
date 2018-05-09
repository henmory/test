package com.example.junit5.mockdemo;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
public class MockDemo {
    //验证某些行为
    @Test
    public void verrify_behavior(){
        List mockList = mock(List.class);
        mockList.add("a");
        mockList.clear();
        verify(mockList).add("a");
        verify(mockList).clear();
    }
}
