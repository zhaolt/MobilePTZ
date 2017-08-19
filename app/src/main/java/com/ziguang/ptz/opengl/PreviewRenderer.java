package com.ziguang.ptz.opengl;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zhaoliangtai on 17/3/22.
 */

public class PreviewRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = PreviewRenderer.class.getSimpleName();

    private FullFrameRect mFullFrameRect;

    private int textureID;

    private SurfaceTexture mSurfaceTexture;

    private float[] stMatrix = new float[16];

    private OnGLSurfaceCreatedListener mGLSurfaceCreatedListener;

    public void setGLSurfaceCreatedListener(OnGLSurfaceCreatedListener listener) {
        mGLSurfaceCreatedListener = listener;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mFullFrameRect = new FullFrameRect(new Texture2DProgram());
        textureID = mFullFrameRect.createTextureObj();
        mSurfaceTexture = new SurfaceTexture(textureID);
        if (null != mGLSurfaceCreatedListener) {
            mGLSurfaceCreatedListener.onGLSurfaceCreated(mSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        if (null == mSurfaceTexture || null == mFullFrameRect)
            return;
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(stMatrix);
        mFullFrameRect.drawFrame(textureID, stMatrix);
    }

    public interface OnGLSurfaceCreatedListener {
        void onGLSurfaceCreated(SurfaceTexture surfaceTexture);
    }
}
