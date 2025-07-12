package com.video.imagepicker

object Constants {
    @JvmField
    var FORMAT_IMAGE: ImageTypeList = ImageTypeList()

    class ImageTypeList : ArrayList<String?>() {
        init {
            add(".PNG")
            add(".JPEG")
            add(".jpg")
            add(".png")
            add(".jpeg")
            add(".JPG")
//            add(".GIF");
//            add(".gif");
        }
    }
}
