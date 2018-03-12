package com.study_open_gl.rocky.opengl_cube;

import android.graphics.Shader;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by rocky on 2018/3/11.
 */

public class RockyCube extends Utils {

    private FloatBuffer verBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer indexBuffer;
    private int rockyProgram;
    private int vMatrix;
    private int vPosition;
    private int aColor;

    public RockyCube() {
        initData();//初始化数据
        initShader();//初始化着色器
    }

    private void initShader() {
        //创建着色程序

        rockyProgram = GLES20.glCreateProgram();
        //关联着色程序
        GLES20.glAttachShader(rockyProgram, loadShader(GLES20.GL_VERTEX_SHADER, verShaderSource));
        GLES20.glAttachShader(rockyProgram, loadShader(GLES20.GL_FRAGMENT_SHADER, fragShaderSource));
        //链接着色程序
        GLES20.glLinkProgram(rockyProgram);
        //获取GLSL 变量ID
        vPosition = GLES20.glGetAttribLocation(rockyProgram, "vPosition");
        aColor = GLES20.glGetAttribLocation(rockyProgram, "aColor");
        vMatrix = GLES20.glGetUniformLocation(rockyProgram, "vMatrix");


    }

    private void initData() {
        //在这里 我们要获得 顶点相关数据的 缓冲
        verBuffer = getFloatBuffer(verPositions);
        colorBuffer = getFloatBuffer(vColor);
        indexBuffer = getShortBuffer(vIndex);
    }


    //设置 矩阵变换的所用到的矩阵

    public static float[] mProjMatrix = new float[16];//投影矩阵
    public static float[] mVMatrix = new float[16];//相机位置
    public static float[] mMVPMatrix;//总变换矩阵
    private static float[] mMMatrix = new float[16];//旋转矩阵

    public float xAngle = 0;//绕x轴旋转的角度

    public void drawPhoto() {
        xAngle = (xAngle - 1) % 360;
        //使用着色程序

        GLES20.glUseProgram(rockyProgram);
        //初始化变化矩阵
        //初始化旋转矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        //平移 z 方向
        Matrix.translateM(mMMatrix, 0, 0, 0, 1);
        //旋转
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 1, 1);

        //给GLSL 变量赋值

        GLES20.glUniformMatrix4fv(vMatrix,1,false,getMatrix(mMMatrix),0);
        //设置画笔
        GLES20.glVertexAttribPointer(vPosition,3,GLES20.GL_FLOAT,false,0,verBuffer);
        GLES20.glVertexAttribPointer(aColor,4,GLES20.GL_FLOAT,false,0,colorBuffer);

        //开启顶点数据
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glEnableVertexAttribArray(aColor);
        //绘制
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,vCount,GLES20.GL_UNSIGNED_SHORT,indexBuffer);

    }

    private float[] getMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix,0,mVMatrix,0,spec,0);
        Matrix.multiplyMM(mMVPMatrix,0,mProjMatrix,0,mMVPMatrix,0);
        return mMVPMatrix;
    }
}
