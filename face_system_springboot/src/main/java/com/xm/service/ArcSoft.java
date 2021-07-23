package com.xm.service;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.*;
import com.arcsoft.face.toolkit.ImageInfo;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;


@Service
public class ArcSoft {


    public static FaceEngine faceEngine(){
        //        从官网获取
        String appId = "xxxx";
        String sdkKey = "xx";
        FaceEngine faceEngine = new FaceEngine("C:\\Users\\xm\\Desktop\\人脸识别\\ArcSoft_ArcFace_Java_Windows_x64_V3.0\\libs\\WIN64");
        int errorCode = faceEngine.active(appId, sdkKey);


        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("引擎激活失败");
        }






        ActiveFileInfo activeFileInfo=new ActiveFileInfo();
        errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("获取激活文件信息失败");
        }
        System.out.println(errorCode);


        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(10);
        engineConfiguration.setDetectFaceScaleVal(16);
        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);


        //初始化引擎
        errorCode = faceEngine.init(engineConfiguration);

        if (errorCode != ErrorInfo.MOK.getValue()) {
            System.out.println("初始化引擎失败");
        }
        return faceEngine;
    }



    public float faceRecognition(String path1, String path2,FaceEngine faceEngine){
        int errorCode;
        //人脸检测1
        ImageInfo imageInfo = getRGBData(new File(path1));
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);

        //人脸检测2
        ImageInfo imageInfo2 = getRGBData(new File(path2));
        List<FaceInfo> faceInfoList2 = new ArrayList<FaceInfo>();
        errorCode = faceEngine.detectFaces(imageInfo2.getImageData(), imageInfo2.getWidth(), imageInfo2.getHeight(),imageInfo.getImageFormat(), faceInfoList2);
        if (faceInfoList.size()==0 || faceInfoList2.size()==0){
            return 0;
        }
        else {
            //特征提取1
            FaceFeature faceFeature = new FaceFeature();
            errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
            System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
            byte[] tz = faceFeature.getFeatureData();

            //特征提取2
            FaceFeature faceFeature2 = new FaceFeature();
            errorCode = faceEngine.extractFaceFeature(imageInfo2.getImageData(), imageInfo2.getWidth(), imageInfo2.getHeight(), imageInfo.getImageFormat(), faceInfoList2.get(0), faceFeature2);
            System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
            //特征比对
            FaceFeature targetFaceFeature = new FaceFeature();
            targetFaceFeature.setFeatureData(faceFeature.getFeatureData());
            FaceFeature sourceFaceFeature = new FaceFeature();
            sourceFaceFeature.setFeatureData(faceFeature2.getFeatureData());
            FaceSimilar faceSimilar = new FaceSimilar();
            errorCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
            System.out.println("相似度：" + faceSimilar.getScore());
            return faceSimilar.getScore();
        }
    }
}
