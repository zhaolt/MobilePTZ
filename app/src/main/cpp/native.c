#include <stdint.h>

#ifdef ANDROID

#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>
#include "jpeglib.h"

#define LOGE(format, ...)  __android_log_print(ANDROID_LOG_ERROR, "(>_<)", format, ##__VA_ARGS__)
#define LOGI(format, ...)  __android_log_print(ANDROID_LOG_INFO,  "(^_^)", format, ##__VA_ARGS__)
#else
#define LOGE(format, ...)  printf("(>_<) " format "\n", ##__VA_ARGS__)
#define LOGI(format, ...)  printf("(^_^) " format "\n", ##__VA_ARGS__)
#endif

int write_JPEG_file(const char *filename, unsigned char *yData, unsigned char *uData,
                    unsigned char *vData, int quality, int image_width, int image_height);

static jclass g_cls_FFmpegInterface = NULL;



void writeJpegFile(JNIEnv* env, jobject jobj, jstring fileName, jbyteArray yDataArr,
                   jbyteArray uDataArr, jbyteArray vDataArr, jint quality, jint width, jint height) {
    char filename[500] = {0};
    sprintf(filename, "%s", (*env)->GetStringUTFChars(env, fileName, NULL));
    jbyte* yData = (*env)->GetByteArrayElements(env, yDataArr, 0);
    jbyte* uData = (*env)->GetByteArrayElements(env, uDataArr, 0);
    jbyte* vData = (*env)->GetByteArrayElements(env, vDataArr, 0);
    int ret = write_JPEG_file(filename, yData, uData, vData, quality, width, height);
    if (ret == -1) {
        LOGE("error: open file failed.");
    }
    (*env)->ReleaseByteArrayElements(env, yDataArr, yData, 0);
    (*env)->ReleaseByteArrayElements(env, uDataArr, uData, 0);
    (*env)->ReleaseByteArrayElements(env, vDataArr, vData, 0);
    (*env)->DeleteLocalRef(env, yData);
    (*env)->DeleteLocalRef(env, uData);
    (*env)->DeleteLocalRef(env, vData);
}

int write_JPEG_file(const char *filename, unsigned char *yData, unsigned char *uData,
                    unsigned char *vData, int quality, int image_width, int image_height) {
    struct jpeg_compress_struct cinfo;
    struct jpeg_error_mgr jerr;

    FILE *outfile;
    JSAMPIMAGE buffer;
    unsigned char *pSrc, *pDst;
    int band, i, buf_width[3], buf_height[3];
    cinfo.err = jpeg_std_error(&jerr);
    jpeg_create_compress(&cinfo); \
    if ((outfile = fopen(filename, "wb")) == NULL) {
        return -1;
    }
    jpeg_stdio_dest(&cinfo, outfile);
    cinfo.image_width = image_width;  // image width and height, in pixels
    cinfo.image_height = image_height;
    cinfo.input_components = 3;    // # of color components per pixel
    cinfo.in_color_space = JCS_RGB;  //colorspace of input image

    jpeg_set_defaults(&cinfo);
    jpeg_set_quality(&cinfo, quality, TRUE);

    cinfo.raw_data_in = TRUE;
    cinfo.jpeg_color_space = JCS_YCbCr;
    cinfo.comp_info[0].h_samp_factor = 2;
    cinfo.comp_info[0].v_samp_factor = 2;

    jpeg_start_compress(&cinfo, TRUE);

    buffer = (JSAMPIMAGE) (*cinfo.mem->alloc_small)((j_common_ptr) &cinfo,
                                                    JPOOL_IMAGE, 3 * sizeof(JSAMPARRAY));

    for (band = 0; band < 3; band++) {
        buf_width[band] = cinfo.comp_info[band].width_in_blocks * DCTSIZE;
        buf_height[band] = cinfo.comp_info[band].v_samp_factor * DCTSIZE;
        buffer[band] = (*cinfo.mem->alloc_sarray)((j_common_ptr) &cinfo,
                                                  JPOOL_IMAGE, buf_width[band], buf_height[band]);
    }
    unsigned char *rawData[3];
    rawData[0] = yData;
    rawData[1] = uData;
    rawData[2] = vData;

    int src_width[3], src_height[3];
    for (i = 0; i < 3; i++) {
        src_width[i] = (i == 0) ? image_width : image_width / 2;
        src_height[i] = (i == 0) ? image_height : image_height / 2;
    }
    int max_line = cinfo.max_v_samp_factor * DCTSIZE;
    int counter;
    for (counter = 0; cinfo.next_scanline < cinfo.image_height; counter++) {
        //buffer image copy.
        for (band = 0; band < 3; band++) {  //每个分量分别处理
            int mem_size = src_width[band];//buf_width[band];
            pDst = (unsigned char *) buffer[band][0];
            pSrc = (unsigned char *) rawData[band] + counter * buf_height[band] *
                                                     src_width[band];//buf_width[band];  //yuv.data[band]分别表示YUV起始地址

            for (i = 0; i < buf_height[band]; i++) { //处理每行数据
                memcpy(pDst, pSrc, mem_size);
                pSrc += src_width[band];//buf_width[band];
                pDst += buf_width[band];
            }
        }
        jpeg_write_raw_data(&cinfo, buffer, max_line);
    }
    jpeg_finish_compress(&cinfo);
    fclose(outfile);
    jpeg_destroy_compress(&cinfo);
    return 0;
}


const JNINativeMethod g_methods[] = {
        "writeJpegFile", "(Ljava/lang/String;[B[B[BIII)V", (void*) writeJpegFile
};

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jclass cls = NULL;
    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    cls = (*env)->FindClass(env, "com/ziguang/ptz/core/jpeglib/JpegNativeInterface");
    // 创建全局弱引用
    g_cls_FFmpegInterface = (*env)->NewWeakGlobalRef(env, cls);
    // 删除本地引用
    (*env)->DeleteLocalRef(env, cls);
    // 绑定Java函数
    (*env)->RegisterNatives(env, g_cls_FFmpegInterface, g_methods, sizeof(g_methods) / sizeof(g_methods[0]));
    return JNI_VERSION_1_6;
}

void JNI_OnUnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
    // 反注册java函数
    (*env)->UnregisterNatives(env, g_cls_FFmpegInterface);
    // 删除全局弱引用
    (*env)->DeleteWeakGlobalRef(env, g_cls_FFmpegInterface);
}
