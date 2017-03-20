package com.example.administrator.myapplication;

import com.example.administrator.myapplication.http.Schedule;

import org.junit.Test;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Schedule schedule = new Schedule();
        schedule.start();
    }
}