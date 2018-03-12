package com.study_open_gl.rocky.opengl_cube;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by rocky on 2018/3/11.
 */

public class RockySurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private RockyCube cube;

    public RockySurfaceView(Context context) {
        super(context);
        initRender();
    }

    private void initRender() {
        //这次我们用2.0 注意 有什么异同
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //设置背景
        GLES20.glClearColor(0, 0, 0, 1);
        //深度测试开启
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        cube = new RockyCube();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        //设置视口
        GLES20.glViewport(0, 0, width, height);

        float r = (float) width / height;
        //设置透视矩阵
        Matrix.frustumM(RockyCube.mProjMatrix, 0, -r, r, -1, 1, 1, 20);
        //设置相机
        Matrix.setLookAtM(RockyCube.mVMatrix,0,
               0,0,8.0f,
                0,0,0,
                1.0f,1.0f,1.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        //清除颜色和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //绘制
        cube.drawPhoto();
    }
}
