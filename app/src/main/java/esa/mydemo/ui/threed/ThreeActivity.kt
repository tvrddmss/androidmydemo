package esa.mydemo.ui.threed

import android.opengl.GLSurfaceView
import android.os.Bundle
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.ui.threed.zft.MyRenderer

class ThreeActivity : AppBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        //R.layout.activity_main三角形
//        val glSurfaceView = OneGlSurfaceView(this)
//        setContentView(glSurfaceView)

        // 创建一个GLSurfaceView，用于显示OpenGL绘制的图形
        // 创建一个GLSurfaceView，用于显示OpenGL绘制的图形
        val glView = GLSurfaceView(this)
        // 创建GLSurfaceView的内容绘制器
        // 创建GLSurfaceView的内容绘制器
        val myRender = MyRenderer(this)
        // 为GLSurfaceView设置绘制器
        // 为GLSurfaceView设置绘制器
        glView.setRenderer(myRender)
        setContentView(glView)
    }


}