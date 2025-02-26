package com.kworkapp.audiogid.utils

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.kworkapp.audiogid.R
import com.kworkapp.audiogid.ui.presentation.launcher.pojo.Data
import com.kworkapp.audiogid.ui.presentation.launcher.pojo.DescriptionItem
import com.kworkapp.audiogid.ui.presentation.launcher.pojo.Section
import com.walhalla.ui.DLog.d
import com.walhalla.ui.DLog.handleException
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.util.LinkedList
import java.util.Locale

object TextUtils {
    @JvmStatic
    fun convertToMilliseconds(time: String): Int {
        val parts = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        val seconds = parts[2].toInt()
        return (hours * 3600 + minutes * 60 + seconds) * 1000
    }

    private fun loadTimingsFromCsv(context: Context): List<DescriptionItem> {
        val list: MutableList<DescriptionItem> = LinkedList()
        try {
            val `is` = context.resources.openRawResource(R.raw.descriptions)
            try {
                BufferedReader(InputStreamReader(`is`)).use { reader ->
                    var line: String
                    while ((reader.readLine().also { line = it }) != null) {
                        val m =
                            line.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val total = m.size
                        if (total == 2 || total == 3) {
                            val name = m[0]
                            val description = m[1]
                            var link = ""
                            if (total > 2) {
                                link = m[2]
                            }
                            //DLog.d("[" + total + "] " + Arrays.toString(m));
                            val item = DescriptionItem(name, description, link)
                            list.add(item)
                        }
                    }
                }
            } catch (e: FileNotFoundException) {
                handleException(e)
            } catch (e: IOException) {
                handleException(e)
            }
        } catch (e: Exception) {
            handleException(e)
            Toast.makeText(context, "Failed to load timings", Toast.LENGTH_SHORT).show()
            return list
        }
        return list
    }

    @JvmStatic
    fun timingAndDescription(context: Context): List<Section> {
        var global = 0
        val `is` = context.resources.openRawResource(R.raw.timing)
        val reader = InputStreamReader(`is`)
        val data = Gson().fromJson(
            reader,
            Data::class.java
        )
        val sections = data.sections
        val descriptionItems = loadTimingsFromCsv(context)

        for (i in sections.indices) {
            val section = sections[i]
            val m = section.items
            val total = m!!.size
            section.total = total
            for (k in 0 until total) {
                val item = m[k]
                val desc = descriptionItems[global]
                item.description = desc.description
                //item.name = item.name + "|" + desc.name;
                item.descriptionName = desc.name
                item.link = desc.link
                global++
            }
        }
        return sections
    }

    fun getJpgFilesFromAssets2(
        context: Context, path: String,
        sectionNameFromDescription: String
    ): List<String> {
        val jpgFiles: MutableList<String> = ArrayList()

        val slc = sectionNameFromDescription.lowercase(Locale.getDefault())

        val am = context.assets
        try {
            val folders = am.list(path)
            d(folders.contentToString() + "@@" + path)

            if (folders != null) {
                for (folder in folders) {
                    if (slc.contains(folder.lowercase(Locale.getDefault()))) {
                        val newPath = "$path/$folder"
                        val files = am.list(newPath)
                        if (files != null) {
                            for (file in files) {
                                if (file.endsWith(".jpg")
                                    || file.endsWith(".png")
                                    || file.endsWith(".jpeg")
                                ) {
                                    val file0 =
                                        "file:///android_asset/$newPath/$file"
                                    jpgFiles.add(file0)
                                    d(file0)
                                }
                            }
                        }
                        break
                    }
                }
            }
        } catch (e: IOException) {
            handleException(e)
        }
        return jpgFiles
    } //    private List<String> getJpgFilesFromAssets(Context context, String path) {
    //        List<String> jpgFiles = new ArrayList<>();
    //        AssetManager assetManager = context.getAssets();
    //        try {
    //            String[] files = assetManager.list(path);
    //            if (files != null) {
    //                for (String file : files) {
    //                    if (file.endsWith(".jpg")) {
    //                        String file0 = "file:///android_asset/" + path + "/" + file;
    //                        jpgFiles.add(file0);
    //                        //DLog.d(file0);
    //                    }
    //                }
    //            }
    //        } catch (IOException e) {
    //            DLog.handleException(e);
    //        }
    //        return jpgFiles;
    //    }
    //    public static List<Section> loadTimingsFromJson0(Context context) {
    //        try {
    //            InputStream is = context.getResources().openRawResource(R.raw.timing);
    //            InputStreamReader reader = new InputStreamReader(is);
    //            Data data = new Gson().fromJson(reader, Data.class);
    //            List<Section> mm = data.sections;
    ////            for (Section section : mm) {
    ////                section.items.size();
    ////                @@@
    ////            }
    //            return mm;
    //        } catch (Exception e) {
    //            Toast.makeText(context, "Failed to load timings", Toast.LENGTH_SHORT).show();
    //            return null;
    //        }
    //    }
    //объект;текст;ссылка;
    //    public static Map<String, Item> loadTimingsFromCsv(Context context) {
    //        Map<String, Item> map = new LinkedHashMap<>();
    //        try {
    //            InputStream is = context.getResources().openRawResource(R.raw.descriptions);
    //            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
    //                String line;
    //                while ((line = reader.readLine()) != null) {
    //                    String[] m = line.split(";");
    //                    int total = m.length;
    //                    if (total == 2 || total == 3) {
    //                        String name = m[0];
    //                        String description = m[1];
    //                        String link = "";
    //                        if (total > 2) {
    //                            link = m[2];
    //                        }
    //                        DLog.d("[" + total + "] " + Arrays.toString(m));
    //                        Item item = new Item(name, description, link);
    //                        map.put(name, item);
    //                    }
    //                }
    //            } catch (FileNotFoundException e) {
    //                DLog.handleException(e);
    //            } catch (IOException e) {
    //                DLog.handleException(e);
    //            }
    //        } catch (Exception e) {
    //            DLog.handleException(e);
    //            Toast.makeText(context, "Failed to load timings", Toast.LENGTH_SHORT).show();
    //            return null;
    //        }
    //        return map;
    //    }
}
