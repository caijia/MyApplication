package com.example.administrator.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.myapplication.widget.DataPoint;
import com.example.administrator.myapplication.widget.GraphView;
import com.example.administrator.myapplication.widget.LineDataSeries;
import com.example.administrator.myapplication.widget.RectDataSeries;
import com.example.administrator.myapplication.widget.ViewPort;

/**
 * Created by cai.jia on 2017/3/20 0020
 */

public class GraphActivity extends AppCompatActivity implements GraphView.OnGraphPointClickListener {

    private GraphView graphView;

    private float getX() {
        return (float) (Math.random() * 12 + 1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        graphView = (GraphView) findViewById(R.id.graph_view);

        LineDataSeries lineDataSeries = new LineDataSeries();
        lineDataSeries.addDataPoint(getLineDataPoint(getX(), 1));
        lineDataSeries.addDataPoint(getLineDataPoint(getX(), 2));
        lineDataSeries.addDataPoint(getLineDataPoint(getX(), 3));
        lineDataSeries.addDataPoint(getLineDataPoint(getX(), 4));
        lineDataSeries.addDataPoint(getLineDataPoint(getX(), 1f));
        lineDataSeries.addDataPoint(getLineDataPoint(getX(), 2f));
        lineDataSeries.addDataPoint(getLineDataPoint(getX(), 3f));
        lineDataSeries.addDataPoint(getLineDataPoint(getX(), 4f));
        lineDataSeries.addDataPoint(getLineDataPoint(getX(), 4.2f));
        lineDataSeries.setWidth(3);
        lineDataSeries.setColor(Color.BLUE);
        graphView.addLineSeries(lineDataSeries);


        RectDataSeries rectDataSeries = new RectDataSeries(Color.DKGRAY,0.1f);
        rectDataSeries.addDataPoint(getLineDataPoint(1, 1.5f));
        rectDataSeries.addDataPoint(getLineDataPoint(2, 2.9f));
        rectDataSeries.addDataPoint(getLineDataPoint(3, 3.4f));
        rectDataSeries.addDataPoint(getLineDataPoint(4, 4.3f));
        rectDataSeries.addDataPoint(getLineDataPoint(5, 3.2f));
//        graphView.addRectSeries(rectDataSeries);

        RectDataSeries rectDataSeries1 = new RectDataSeries(Color.GRAY,0.1f);
        rectDataSeries1.addDataPoint(getLineDataPoint(1, 1.8f));
        rectDataSeries1.addDataPoint(getLineDataPoint(2, 3.9f));
        rectDataSeries1.addDataPoint(getLineDataPoint(3, 2.4f));
        rectDataSeries1.addDataPoint(getLineDataPoint(4, 3.9f));
        rectDataSeries1.addDataPoint(getLineDataPoint(5, 2.2f));
//        graphView.addRectSeries(rectDataSeries1);
        ViewPort viewPort = new ViewPort();
        viewPort.setTextArrayY(new String[]{"0","0.25","0.50","0.75","1.00"});

        viewPort.setTextArrayX(new String[]{"0","14:00","14:15","14:30","14:45","15:00",
                "15:15","15:30","15:45","16:00","16:15","16:30","16:45"});

        viewPort.setSpacingY(50*3);
        viewPort.setSpacingX(40*3);
        graphView.setViewPort(viewPort);//y

        graphView.addRectSeries(rectDataSeries);
        graphView.setOnGraphPointClickListener(this);
    }

    public void addRect(View view) {
        int[] colorArray = {Color.CYAN, Color.RED, Color.GREEN};
        int color = colorArray[(int) (Math.random() * 3)];
        RectDataSeries rectDataSeries1 = new RectDataSeries(color,0.1f);
        rectDataSeries1.addDataPoint(getLineDataPoint(1, 1.8f));
        rectDataSeries1.addDataPoint(getLineDataPoint(2, 3.9f));
        rectDataSeries1.addDataPoint(getLineDataPoint(3, 2.4f));
        rectDataSeries1.addDataPoint(getLineDataPoint(4, 3.9f));
        rectDataSeries1.addDataPoint(getLineDataPoint(5, 2.2f));
        graphView.addRectSeries(rectDataSeries1);
    }

    private DataPoint getLineDataPoint(final float x, final float y) {
        return new LineDataPoint(x, y);
    }

    @Override
    public void onGraphPointClick(DataPoint point) {
        String s = "x=" + point.getX() + "---y=" + point.getY();
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private class LineDataPoint implements DataPoint {

        private float x;
        private float y;

        public LineDataPoint(float x, float y) {

            this.x = x;
            this.y = y;
        }

        @Override
        public float getX() {
            return x;
        }

        @Override
        public float getY() {
            return y;
        }

    }

}
