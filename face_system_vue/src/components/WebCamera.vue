<template>
  <div class="myLive">
    <div class="top_button">
      <!--开启摄像头-->
      <Button
        v-if="isCameraFlag"
        type="primary"
        ghost
        @click="callCamera"
        style="margin-right: 10px"
        >开始检测</Button
      >
      <!--关闭摄像头-->
      <Button
        v-else
        type="danger"
        ghost
        @click="closeCamera"
        style="margin-right: 10px"
        >关闭检测</Button
      >
      <!--拍照-->
      <!-- <Button type="primary" @click="photograph"></Button> -->
    </div>
    <!--图片展示-->
    <video
      ref="video"
      width="500"
      height="376"
      autoplay
      class="mt20"
      style="background: #d9d9d9"
    ></video>
    <!--canvas截取流-->
    <canvas ref="canvas" width="500" height="376" v-if="isImg" ></canvas>
    <p v-if="isImg">人脸相似度为：{{ acc }}</p>
  </div>
</template>



<script>
export default {
  name: "myLive",
  data() {
    return {
      isCameraFlag: true,
      base64: "",
      acc: "",
      str1: "",
      isImg: false,
    };
  },

  // 循环控制器
  mounted() {
    // this.timer = setInterval(this.photograph, 1000);
    this.timer = setInterval(() => {
      // 要执行的函数
      // console.log(this.isImg)
      if (this.isImg) {
        this.photograph();
      }
    }, 1000);
  },

  methods: {
    // 调用摄像头
    callCamera() {
      this.isImg = true
      this.isCamera = true;
      console.log(this.isCamera);
      // H5调用电脑摄像头API
      navigator.mediaDevices
        .getUserMedia({
          audio: true, //开启音频
          video: true, //开启视频
        })
        .then((success) => {
          var videoTracks = success.getVideoTracks();
          this.$refs["video"].srcObject = success;
          this.isCameraFlag = false;
          // 实时拍照效果
          this.$refs["video"].play();
        })
        .catch((error) => {
          console.error("摄像头开启失败，请检查摄像头是否可用！");
          this.isCameraFlag = true;
        });
    },

    // 拍照
    async photograph() {
      let ctx = this.$refs["canvas"].getContext("2d");
      ctx.drawImage(this.$refs["video"], 0, 0, 500, 376);
      let imgBase64 = this.$refs["canvas"].toDataURL("image/jpeg", 0.7);
      let str = imgBase64.replace("data:image/jpeg;base64,", "");
      this.base64 = str;
      const { data: res } = await this.$http.post("/img", this.base64);
 
      this.acc = res.acc;
    },

    // 关闭摄像头
    closeCamera() {
      this.isImg=false
      if (!this.$refs["video"].srcObject) return;
      let stream = this.$refs["video"].srcObject;
      let tracks = stream.getTracks();
      tracks.forEach((track) => {
        track.stop();
      });
      this.$refs["video"].srcObject = null;
      this.isCameraFlag = true;
    },
  },
};
</script>