#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include "jpeg/jpeglib.h"
#import <omp.h>

#ifdef ANDROID

#include <jni.h>
#include <android/log.h>

#define LOGE(format, ...)  __android_log_print(ANDROID_LOG_ERROR, "(>_<)", format, ##__VA_ARGS__)
#define LOGI(format, ...)  __android_log_print(ANDROID_LOG_INFO,  "(^_^)", format, ##__VA_ARGS__)
#else
#define LOGE(format, ...)  printf("(>_<) " format "\n", ##__VA_ARGS__)
#define LOGI(format, ...)  printf("(^_^) " format "\n", ##__VA_ARGS__)
#endif

int write_JPEG_file(const char *filename, unsigned char *yData, unsigned char *uData,
                    unsigned char *vData, int quality, int image_width, int image_height);

void Java_com_ziguang_ptz_core_jpeglib_JpegNativeInterface_writeJpegFile(JNIEnv *env, jobject jobj,
                                                                         jstring fileName,
                                                                         jobject yBuffer,
                                                                         jint yLen,
                                                                         jobject cbBuffer,
                                                                         jint cbLen,
                                                                         jobject crBuffer,
                                                                         jint uvStride,
                                                                         jint quality,
                                                                         jint width, jint height) {
    char *filename[500] = {0};
    sprintf(filename, "%s", (*env)->GetStringUTFChars(env, fileName, NULL));
    jbyte *y = (*env)->GetDirectBufferAddress(env, yBuffer);
    jbyte *cb = (*env)->GetDirectBufferAddress(env, cbBuffer);
    jbyte *cr = (*env)->GetDirectBufferAddress(env, crBuffer);
    uint8_t *uData = malloc(cbLen);
    uint8_t *vData = malloc(cbLen);
    int j, k;

    int uLimit = 0;
    int vLimit = 0;
    if (uvStride == 2) { // yuv420 sp uv交错
#pragma omp parallel for num_threads(4)
        for (j = 0; j < cbLen; j++) {
            if (j % 2 == 0) {
                uData[uLimit++] = cb[j];
            } else {
                vData[vLimit++] = cb[j];
            }
        }
#pragma omp parallel for num_threads(4)
        for (k = 0; k < cbLen; k++) {
            if (k % 2 == 0) {
                uData[uLimit++] = cr[k];
            } else {
                vData[vLimit++] = cr[k];
            }
        }
        write_JPEG_file(filename, y, uData, vData, quality, width, height);
    } else {    // yuv420p
        write_JPEG_file(filename, y, cb, cr, quality, width, height);
    }

    free(uData);
    free(vData);
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
#pragma omp parallel for num_threads(4)
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
#pragma omp parallel for num_threads(4)
    for (i = 0; i < 3; i++) {
        src_width[i] = (i == 0) ? image_width : image_width / 2;
        src_height[i] = (i == 0) ? image_height : image_height / 2;
    }
    int max_line = cinfo.max_v_samp_factor * DCTSIZE;
    int counter;
#pragma omp parallel for num_threads(4)
    for (counter = 0; cinfo.next_scanline < cinfo.image_height; counter++) {
        //buffer image copy.
#pragma omp parallel for num_threads(4)
        for (band = 0; band < 3; band++) {  //每个分量分别处理
            int mem_size = src_width[band];//buf_width[band];
            pDst = (unsigned char *) buffer[band][0];
            pSrc = (unsigned char *) rawData[band] + counter * buf_height[band] *
                                                     src_width[band];//buf_width[band];  //yuv.data[band]分别表示YUV起始地址
#pragma omp parallel for num_threads(4)
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
