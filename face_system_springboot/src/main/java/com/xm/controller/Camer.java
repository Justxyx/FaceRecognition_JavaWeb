package com.xm.controller;

import com.arcsoft.face.FaceEngine;
import com.xm.enity.Msg;
import com.xm.service.ArcSoft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


@Controller
public class Camer {

    @Autowired
    ArcSoft arcSoft;
    FaceEngine faceEngine = arcSoft.faceEngine();

    @Autowired
    Msg msg;


    @CrossOrigin
    @ResponseBody
    @PostMapping("/img")
    public Msg img(@RequestBody String base64Img){

        String base64 = base64Img.replace("%2F", "/");
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] data = new byte[0];
        try {
            data = decoder.decodeBuffer(base64);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i =0 ; i < data.length ;i++) {
            if(data[i] < 0) {
                data[i] += 256;
            }
        }
        //写入保存成jpeg文件
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("D:\\人脸识别模拟服务器\\test2.jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

        float v = arcSoft.faceRecognition("D:\\人脸识别模拟服务器\\test2.jpeg", "D:\\人脸识别模拟服务器\\test.jpeg",faceEngine);
        System.out.println("相似度为" + v);
        if (v==0){
            msg.setCode(400);
            msg.setMsg("请放入正确的人脸");
            msg.setAcc(0);
        }else {
            msg.setCode(200);
            msg.setMsg("识别正确");
            msg.setAcc(v);
        }
        return msg;
    }

}
