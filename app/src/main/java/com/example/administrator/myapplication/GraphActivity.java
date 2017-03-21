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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

         graphView = (GraphView) findViewById(R.id.graph_view);

        LineDataSeries lineDataSeries = new LineDataSeries();
        lineDataSeries.addDataPoint(getLineDataPoint(1, 1));
        lineDataSeries.addDataPoint(getLineDataPoint(2, 2));
        lineDataSeries.addDataPoint(getLineDataPoint(3, 3));
        lineDataSeries.addDataPoint(getLineDataPoint(4, 4));
        lineDataSeries.addDataPoint(getLineDataPoint(5, 5));
        lineDataSeries.addDataPoint(getLineDataPoint(12, 4));
        lineDataSeries.setWidth(3);
        lineDataSeries.setColor(Color.BLUE);
        graphView.addLineSeries(lineDataSeries);

        LineDataSeries lineDataSeries1 = new LineDataSeries();
        lineDataSeries1.addDataPoint(getLineDataPoint(1, 2.3f));
        lineDataSeries1.addDataPoint(getLineDataPoint(2, 3.2f));
        lineDataSeries1.addDataPoint(getLineDataPoint(3, 4.3f));
        lineDataSeries1.addDataPoint(getLineDataPoint(4, 5.2f));
        lineDataSeries1.addDataPoint(getLineDataPoint(5, 6.1f));
        lineDataSeries1.addDataPoint(getLineDataPoint(11, 5));
        lineDataSeries1.setWidth(3);
        lineDataSeries1.setColor(Color.RED);
        graphView.addLineSeries(lineDataSeries1);

        RectDataSeries rectDataSeries = new RectDataSeries(Color.DKGRAY,0.1f);
        rectDataSeries.addDataPoint(getLineDataPoint(1, 1.5f));
        rectDataSeries.addDataPoint(getLineDataPoint(2, 2.9f));
        rectDataSeries.addDataPoint(getLineDataPoint(3, 3.4f));
        rectDataSeries.addDataPoint(getLineDataPoint(4, 4.9f));
        rectDataSeries.addDataPoint(getLineDataPoint(5, 5.2f));
        rectDataSeries.addDataPoint(getLineDataPoint(12, 4));
//        graphView.addRectSeries(rectDataSeries);

        RectDataSeries rectDataSeries1 = new RectDataSeries(Color.GRAY,0.1f);
        rectDataSeries1.addDataPoint(getLineDataPoint(1, 1.8f));
        rectDataSeries1.addDataPoint(getLineDataPoint(2, 3.9f));
        rectDataSeries1.addDataPoint(getLineDataPoint(3, 2.4f));
        rectDataSeries1.addDataPoint(getLineDataPoint(4, 3.9f));
        rectDataSeries1.addDataPoint(getLineDataPoint(5, 2.2f));
        rectDataSeries1.addDataPoint(getLineDataPoint(12, 4));
//        graphView.addRectSeries(rectDataSeries1);
        ViewPort viewPort = new ViewPort(
                0, 20, 56 * 3, 12  //x
                , 0.6f, 1f, 20 * 3, 8);
        viewPort.setxTextArray(new String[]{"14:00","14:00","14:00","14:00","14:00","14:00",
                "14:00","14:00","14:00","14:00","14:00","14:00","14:00"});
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
        rectDataSeries1.addDataPoint(getLineDataPoint(10, 4));
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

        @Override
        public void setX(float x) {
            this.x = x;
        }

        @Override
        public void setY(float y) {
            this.y = y;
        }
    }

}
