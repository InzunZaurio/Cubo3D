package com.example.cubo3d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.hardware.*;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    Perspectiva perspectiva;
    public class Dimension {
        public int height, width;
        public Dimension(int w, int h) {
            this.width = w;
            this.height = h;
        }
        public final void set(Dimension d) {
            this.width = d.width;
            this.height = d.height;
        }
    }
    class Obj {
        float f12d, objSize;
        float phi = 1.3f, rho, theta = 0.3f;
        float v11, v12, v13, v21, v22, v23, v32, v33, v43;
        Point2D[] vScr = new Point2D[8];
        Point3D[] f13w = new Point3D[8];
        Obj() {
            MainActivity mainActivity = MainActivity.this;
            this.f13w[0] = new Point3D(1.0d, -1.0d, -1.0d);
            this.f13w[1] = new Point3D(1.0d, 1.0d, -1.0d);
            this.f13w[2] = new Point3D(-1.0d, 1.0d, -1.0d);
            this.f13w[3] = new Point3D(-1.0d, -1.0d, -1.0d);
            this.f13w[4] = new Point3D(1.0d, -1.0d, 1.0d);
            this.f13w[5] = new Point3D(1.0d, 1.0d, 1.0d);
            this.f13w[6] = new Point3D(-1.0d, 1.0d, 1.0d);
            this.f13w[7] = new Point3D(-1.0d, -1.0d, 1.0d);
            this.objSize = (float) Math.sqrt(12.0d);
            this.rho = this.objSize * 5.0f;
        }
        void initPersp() {
            float costh = (float) Math.cos((double) this.theta), sinth = (float) Math.sin((double)
                    this.theta);
            float cosph = (float) Math.cos((double) this.phi), sinph = (float) Math.sin((double)
                    this.phi);
            this.v11 = -sinth;
            this.v12 = (-cosph) * costh;
            this.v13 = sinph * costh;
            this.v21 = costh;
            this.v22 = (-cosph) * sinth;
            this.v23 = sinph * sinth;
            this.v32 = sinph;
            this.v33 = cosph;
            this.v43 = -this.rho;
        }
        void eyeAndScreen() {
            initPersp();
            for (int i = 0; i < 8; i++) {
                Point3D paint = this.f13w[i];
                float z = (((this.v13 * paint.f18x) + (this.v23 * paint.f19y)) + (this.v33 *
                        paint.f20z)) + this.v43;
                this.vScr[i] = new Point2D(((-this.f12d) * ((this.v11 * paint.f18x) + (this.v21 *
                        paint.f19y))) / z, ((-this.f12d) * (((this.v12 * paint.f18x) + (this.v22 * paint.f19y)) + (this.v32
                        * paint.f20z))) / z);
            }
        }
    }
    public class Perspectiva extends View implements SensorEventListener {
        int centerX, centerY;
        Dimension dim;
        Sensor gyro;
        int maxX, maxY, minMaxXY;
        Obj obj = new Obj();
        Paint paint = new Paint();
        SensorManager sensorManager;
        int f14x = 180, f15y = 180; /* renamed from: x, renamed from: y */
        @SuppressLint("WrongConstant")
        public Perspectiva(Context c) {
            super(c);
            this.sensorManager = (SensorManager) MainActivity.this.getSystemService("sensor");
            this.gyro = this.sensorManager.getDefaultSensor(11);
            this.sensorManager.registerListener(this, this.gyro, 0);
            this.paint.setAntiAlias(true);
        }
        protected void onDraw(Canvas c) {
            super.onDraw(c);
            this.paint.setColor(Color.parseColor("#ffffff"));
            c.drawPaint(this.paint);
            this.dim = new Dimension(getWidth() / 2, getHeight());
            this.maxX = this.dim.width - 1;
            this.maxY = this.dim.height - 1;
            this.minMaxXY = Math.min(this.maxX, this.maxY);
            this.centerX = this.maxX / 2;
            this.centerY = this.maxY / 2;
            this.obj.f12d = (this.obj.rho * ((float) this.minMaxXY)) / this.obj.objSize;
            this.obj.eyeAndScreen();
            line(c, 0, 1); line(c, 1, 2); line(c, 2, 3); line(c, 3, 0);
            line(c, 4, 5); line(c, 5, 6); line(c, 6, 7); line(c, 7, 4);
            line(c, 0, 4); line(c, 1, 5); line(c, 2, 6); line(c, 3, 7);
            this.dim = new Dimension((int) (((double) getWidth()) * 1.5d), getHeight());
            this.maxX = this.dim.width - 1;
            this.maxY = this.dim.height - 1;
            this.minMaxXY = Math.min(this.maxX, this.maxY);
            this.centerX = this.maxX / 2;
            this.centerY = this.maxY / 2;
            this.obj.f12d = (this.obj.rho * ((float) this.minMaxXY)) / this.obj.objSize;
            this.obj.eyeAndScreen();
            line(c, 0, 1); line(c, 1, 2); line(c, 2, 3); line(c, 3, 0);
            line(c, 4, 5); line(c, 5, 6); line(c, 6, 7); line(c, 7, 4);
            line(c, 0, 4); line(c, 1, 5); line(c, 2, 6); line(c, 3, 7);
        }
        int iX(float x) {
            return (int) Math.floor((double) (((float) this.centerX) + x));
        }
        int iY(float y) {
            return (int) Math.floor((double) (((float) this.centerY) - y));
        }
        void line(Canvas ca, int i, int j) {
            this.paint.setColor(Color.parseColor("#2196F3"));//ColorRojo ff0000
            Point2D po = this.obj.vScr[i];
            Point2D q = this.obj.vScr[j];
            ca.drawLine((float) iX(po.f16x), (float) iY(po.f17y), (float) iX(q.f16x), (float)
                    iY(q.f17y), this.paint);
        }
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] matrizDeRotacion = new float[16];
            SensorManager.getRotationMatrixFromVector(matrizDeRotacion, sensorEvent.values);
            float[] remapeoMatrizDeRotacion = new float[16];
            SensorManager.remapCoordinateSystem(matrizDeRotacion, 1, 3, remapeoMatrizDeRotacion);
            float[] orientations = new float[3];
            SensorManager.getOrientation(remapeoMatrizDeRotacion, orientations);
            for (int i = 0; i < 3; i++) {
                orientations[i] = (float) Math.toDegrees((double) orientations[i]);
            }
            this.obj.theta = ((float) (getWidth() / 2)) / (((float) this.f14x) + orientations[0]);
            this.obj.phi = ((float) getHeight()) / (((float) this.f15y) + orientations[1]);
            this.obj.rho = (this.obj.phi / this.obj.theta) * ((float) getHeight());
            this.centerX = (int) (((float) this.f14x) + orientations[0]);
            this.centerY = (int) (((float) this.f15y) + orientations[1]);
            invalidate();
        }
        public void onAccuracyChanged(Sensor sensor, int i) { }
    }
    class Point2D {
        float f16x, f17y;
        Point2D(float x, float y) {
            this.f16x = x;
            this.f17y = y;
        }
    }
    class Point3D {
        float f18x, f19y, f20z;
        Point3D(double x, double y, double z) {
            this.f18x = (float) x;
            this.f19y = (float) y;
            this.f20z = (float) z;
        }
    }
    @SuppressLint("WrongConstant")
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setRequestedOrientation(0);
        this.perspectiva = new Perspectiva(this);
        setContentView(this.perspectiva);
    }
}
