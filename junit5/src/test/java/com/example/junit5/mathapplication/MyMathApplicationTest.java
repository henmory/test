package com.example.junit5.mathapplication;

import com.example.junit5.service.IComputer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assume.assumeThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/***
 *
 * Created on：09/05/2018
 *
 * Created by：henmory
 *
 * Description: mock 采用when添加行为，采用verify验证结果
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
        //为mock的对象添加一个行为
        when(iComputer.add(1,2)).thenReturn(3);
        //验证添加行为的结果
        assumeThat(3, equalTo(mathApplication.add(1,2)));
        //验证mock的方法传入的参数是否正确
        verify(iComputer).add(1,2);

        when(iComputer.add(2,3)).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                Object[] params = invocation.getArguments();
                invocation.getMock();
                return 0;
            }
        });
    }

    @Test
    void subtract() {
        //mock的相关函数都是创造条件，目的是为我们的测试对象mathApplication输出中间结果，以便测试它
        when(iComputer.subtract(1,2)).thenReturn(3);
        when(iComputer.subtract(4,1)).thenReturn(3);
        assumeThat(3, equalTo(mathApplication.subtract(1,2)));
        assumeThat(3, equalTo(mathApplication.subtract(1,2)));
        assumeThat(3, equalTo(mathApplication.subtract(1,2)));
        assumeThat(3, equalTo(mathApplication.subtract(1,2)));
        //验证某个方法，某个参数调用的次数==还可以制定最多最少执行的次数
        verify(iComputer, times(4)).subtract(1,2);
        verify(iComputer, never()).subtract(4,1);
    }

    @Test
    void multiply() {
        when(iComputer.multiply(1,2)).thenReturn(3);
        assumeThat(3, equalTo(mathApplication.multiply(1,2)));}

    @Test
    void divide() {
        doThrow(new RuntimeException("exception")).when(iComputer).divide(4,1);
        when(iComputer.divide(1,2)).thenReturn(3);
        assumeThat(3, equalTo(mathApplication.divide(4,1)));}


    @Test
    public void testLinkedListSpyWrong() {
        // Lets mock a LinkedList
        List<String> list = new LinkedList<>();
        List<String> spy = spy(list);

        // this does not work
        // real method is called so spy.get(0)
        // throws IndexOutOfBoundsException (list is still empty)
        when(spy.get(0)).thenReturn("foo");

        assertEquals("foo", spy.get(0));
    }

    @Test
    public void testLinkedListSpyCorrect() {
        // Lets mock a LinkedList
        List<String> list = new LinkedList<>();
        List<String> spy = spy(list);

        // You have to use doReturn() for stubbing
        doReturn("foo").when(spy).get(0);

        assertEquals("foo", spy.get(0));
    }
}