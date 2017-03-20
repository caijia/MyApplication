package com.example.administrator.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.myapplication.widget.DataPoint;
import com.example.administrator.myapplication.widget.GraphView;
import com.example.administrator.myapplication.widget.LineDataSeries;
import com.example.administrator.myapplication.widget.ViewPort;

/**
 * Created by cai.jia on 2017/3/20 0020
 */

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        GraphView graphView = (GraphView) findViewById(R.id.graph_view);

        LineDataSeries lineDataSeries = new LineDataSeries();
        lineDataSeries.addDataPoint(getLineDataPoint(1, 4));
        lineDataSeries.addDataPoint(getLineDataPoint(2, 3));
        lineDataSeries.addDataPoint(getLineDataPoint(8, 3));
        lineDataSeries.addDataPoint(getLineDataPoint(4, 6));
        lineDataSeries.addDataPoint(getLineDataPoint(11, 2));
        lineDataSeries.addDataPoint(getLineDataPoint(16, 4));
        lineDataSeries.setWidth(3);
        lineDataSeries.setColor(Color.BLUE);
        graphView.addLineSeries(lineDataSeries);

        LineDataSeries lineDataSeries1 = new LineDataSeries();
        lineDataSeries1.addDataPoint(getLineDataPoint(2, 5));
        lineDataSeries1.addDataPoint(getLineDataPoint(3, 4));
        lineDataSeries1.addDataPoint(getLineDataPoint(9, 4));
        lineDataSeries1.addDataPoint(getLineDataPoint(5, 3));
        lineDataSeries1.addDataPoint(getLineDataPoint(12, 3));
        lineDataSeries1.addDataPoint(getLineDataPoint(17, 5));
        lineDataSeries1.setWidth(3);
        lineDataSeries1.setColor(Color.RED);
        graphView.addLineSeries(lineDataSeries1);

        graphView.setViewPort(new ViewPort(
                0, 20, 56 * 3, 20  //x
                , 0.6f, 1f, 40 * 3, 8));//y
    }

    private DataPoint getLineDataPoint(final int x, final int y) {
        return new LineDataPoint(x, y);
    }

    private class LineDataPoint implements DataPoint {

        private int x;
        private int y;

        public LineDataPoint() {
        }

        public LineDataPoint(int x, int y) {

            this.x = x;
            this.y = y;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }
    }

}
