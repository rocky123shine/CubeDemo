package com.study_open_gl.rocky.opengl_cube;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by rocky on 2018/3/11.
 */

public abstract class Utils {
    ///设置订单数据

//    这里我们要绘制一个立方体 那么我们需要6个矩形做面
    //在OpenGL ES 中 基本图形 是点、线、三角形
    //那么 第一步 我们要绘制矩形 即需要两个三角形
    //    因此 一个矩形需要的顶点数 verCount = 矩形（两个三角形）*6;
    int vCount = 1*2*3*6;
//    我们 建立空间坐标系  以立方体的中心为原点

//    x方向水平向右 y竖直向上 z 垂直屏幕 向外

//    给面的顶点标号
//    正面 左上0左下1右下2右上3
//    背面 左上4左下5右下6又上7
    public float [] verPositions={

                                // 正面
        -1.0f, 1.0f, 1.0f,  //左上0
        -1.0f, -1.0f, 1.0f,   //左下1
        1.0f, -1.0f, 1.0f,   //右下2
        1.0f, 1.0f, 1.0f,  //右上3
                           //  背面
        -1.0f, 1.0f, -1.0f, //左上4
        -1.0f, -1.0f, -1.0f,  //左下5
        1.0f, -1.0f, -1.0f, //右下6
        1.0f, 1.0f, -1.0f, //又上7
};

    //绘制的起点我们可以所以选择  但是选择索引顺序一旦选定 要全部一致
    //每个面的起始位置都可以任意选择
    //这里我就从小号开始 切选择逆时针
    public short[] vIndex={
            0,3,2,0,2,1,    //第一个面 正面
            4,7,6,4,6,5,//背面
            4,0,1,4,1,5,//左面
            3,7,6,3,6,2,//右面
            0,4,7,0,7,3,//上面
            1,5,6,1,6,2//下面
    };

    public float[] vColor={
            1f, 1f, 1f, 1f,
            0f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 0f, 1f, 1f,
            0f, 0f, 1f, 1f,
            0f, 0f, 0.5f, 1f,
            1f, 1f, 1f, 1f,
            0.5f, 0f, 0f, 1f
    };

    //这次我们写个GLSL
    public final String verShaderSource=
            "attribute vec4 vPosition;" +//声明一个用attribute修饰的变量（顶点）
                    "uniform mat4 vMatrix;" +//总变换矩阵
                    "varying  vec4 vColor;" +//颜色易变变量（成对出现）
                    "attribute vec4 aColor;" +//声明一个用attribute修饰的变量（颜色）
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +//根据总变换的矩阵计算绘制此顶点的位置
                    "  vColor=aColor;" + //将接收的颜色传递给片元着色器
                    "}";

    public final String fragShaderSource=
            "precision mediump float;" +//预定义的全局默认精度
                    "varying vec4 vColor;" +//接收从顶点着色器过来的参数
                    "void main() {" +
                    "  gl_FragColor = vColor;" + //给此片源颜色值
                    "}";



    //提供加载着色器资源的方法

    public int loadShader(int shaderType,String shaderSource ){
        int shader = GLES20.glCreateShader(shaderType);
        if (shader ==0) {
            //创建失败
            return 0;
        }
        //加载资源
        GLES20.glShaderSource(shader,shaderSource);
        //编译
        GLES20.glCompileShader(shader);

        return shader;


    };

    //提供 两个获得缓存数据的方法

    public FloatBuffer getFloatBuffer(float[] data){
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length*4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer fb = buffer.asFloatBuffer();
        fb.put(data);
        fb.position(0);
        return fb;

    }

    public ShortBuffer getShortBuffer(short[] data){
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length*2);
        buffer.order(ByteOrder.nativeOrder());
        ShortBuffer shortBuffer = buffer.asShortBuffer();
        shortBuffer.put(data);
        shortBuffer.position(0);
        return shortBuffer;
    }
}
