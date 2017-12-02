package com.example.liuan.screenview.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import com.example.liuan.screenview.bean.MediaItem;
import com.litesuits.common.assist.Check;
import com.litesuits.common.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class MediaVaultUtil {
    private static final String NO_MEDIA = ".nomedia";
    private static final String SLASH = "/";
    private static final String REPLACED = "%";
    private static final String PHOTO = "photo";
    private static final String VIDEO = "video";

    /**
     * 获取保存文件的父文件夹
     */
    public static File createEncryptedFolder(Context context, boolean isPhoto) {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        File root = Environment.getExternalStorageDirectory();
        String packageName = context.getPackageName();
        File nomediaFolder = null;
        if (isPhoto) {
            nomediaFolder = new File(root, packageName + "/" + PHOTO + "/" + NO_MEDIA);
        } else {
            nomediaFolder = new File(root, packageName + "/" + VIDEO + "/" + NO_MEDIA);
        }
        nomediaFolder.mkdirs();
        File nomediaFile = new File(nomediaFolder, NO_MEDIA);
        try {
            nomediaFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nomediaFolder;
    }

    /**
     * @param path 路径
     * @return 加密后的文件路径
     */
    public static String encryptFilePath(String path) {
        File root = Environment.getExternalStorageDirectory();
        String p = path.replace(root.getAbsolutePath(), "");
        String encrypt = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            GZIPOutputStream gout = new GZIPOutputStream(buffer);
            gout.write(p.getBytes());
            gout.close();
            byte[] result = buffer.toByteArray();
            String t = new String(result);
            encrypt = Base64.encodeToString(result, Base64.NO_WRAP);
            encrypt = encrypt.replaceAll(SLASH, REPLACED);
            encrypt = "." + encrypt;
            gout.close();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encrypt;
    }

    /**
     * @param path 路径
     * @return 解密后的文件路径
     */
    public static String decryptFilePath(String path) {
        String decrypt = null;
        try {
            String p = path.substring(1);
            p = p.replaceAll(REPLACED, SLASH);
            byte[] decode = Base64.decode(p, Base64.NO_WRAP);
            ByteArrayInputStream bais = new ByteArrayInputStream(decode);
            GZIPInputStream gzis = new GZIPInputStream(bais);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int count;
            byte data[] = new byte[256];
            while ((count = gzis.read(data)) != -1) {
                buffer.write(data, 0, count);
            }
            gzis.close();
            byte[] b = buffer.toByteArray();
            decrypt = new String(b);
            File root = Environment.getExternalStorageDirectory();
            decrypt = new File(root, decrypt).getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return decrypt;
    }

    /**
     * @param context
     * @return 获取加密的所有文件保存为PhotoItem实体
     */
    public static List<MediaItem> getEncryptedPhoto(Context context) {
        File root = createEncryptedFolder(context, true);
        if (root.exists() && root.isDirectory()) {
            File[] files = root.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return !filename.endsWith(NO_MEDIA);
                }
            });
            if (files==null) {
                return Collections.EMPTY_LIST;
            }
            List<MediaItem> fileList = new ArrayList<>();//将需要的子文件信息存入到FileInfo里面
            for (File file : files) {
                MediaItem photoItem = new MediaItem();
                photoItem.setName(file.getName());
                photoItem.setPath(file.getAbsolutePath());
                String realPath = decryptFilePath(file.getName());
                try {
                    File realFile = new File(realPath);
                    photoItem.setRealName(realFile.getName());
                    photoItem.setRealPath(realPath);
                    photoItem.setRealParentName(realFile.getParentFile().getName());
                    photoItem.setRealParentPath(realFile.getParent());
                } catch (Exception e) {
                    e.printStackTrace();
                    photoItem.setRealName("unknow");
                    photoItem.setRealPath(null);
                    photoItem.setRealParentName("unknow");
                    photoItem.setRealParentPath(null);
                }
                fileList.add(photoItem);
            }
            Collections.sort(fileList);
            if (fileList != null) {
                return fileList;
            } else {
                return Collections.EMPTY_LIST;
            }
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public static List<MediaItem> getEncryptedVideo(Context context) {
        File root = createEncryptedFolder(context, false);
        if (root.exists() && root.isDirectory()) {
            File[] files = root.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return !filename.endsWith(NO_MEDIA);
                }
            });
            MediaPlayer player = null;
            List<MediaItem> fileList = new ArrayList<>();//将需要的子文件信息存入到FileInfo里面
            if (!Check.isEmpty(files)) {
                for (File file : files) {
                    player = new MediaPlayer();
                    MediaItem videoItem = new MediaItem();
                    videoItem.setName(file.getName());
                    videoItem.setPath(file.getAbsolutePath());
                    String realPath = decryptFilePath(file.getName());
                    try {
                        player.setDataSource(file.getAbsolutePath());
                        player.prepare();
                        videoItem.setDuring(player.getDuration());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.release();
                    try {
                        File realFile = new File(realPath);
                        videoItem.setRealName(realFile.getName());
                        videoItem.setRealPath(realPath);
                        videoItem.setRealParentName(realFile.getParentFile().getName());
                        videoItem.setRealParentPath(realFile.getParent());

                    } catch (Exception e) {
                        e.printStackTrace();
                        videoItem.setRealName("unknow");
                        videoItem.setRealPath(null);
                        videoItem.setRealParentName("unknow");
                        videoItem.setRealParentPath(null);
                    }
                    fileList.add(videoItem);
                }
            }
            Collections.sort(fileList);
            if (fileList != null) {
                return fileList;
            } else {
                return Collections.EMPTY_LIST;
            }
        } else {
            return Collections.EMPTY_LIST;
        }
    }


    /**
     * @param context
     * @return 获取所有的图片文件
     */
    public static List<MediaItem> getDecryptedPhoto(Context context) {
        List<MediaItem> list = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"}, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor != null && cursor.getColumnCount() != 0 && cursor.getCount() > 0) {
            cursor.moveToLast();
            try {
                do {
                    if (cursor.isNull(cursor.getColumnIndex(MediaStore.Images.Media.DATA))) {
                        break;
                    }
                    MediaItem entity = new MediaItem();
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    if (path != null) {
                        File file = new File(path);
                        if (file != null && file.exists() && file.getParentFile() != null) {
                            entity.setPath(path);
                            entity.setRealPath(path);
                            entity.setSelect(false);
                            entity.setName(file.getName());
                            entity.setRealName(file.getName());
                            entity.setRealParentName(file.getParentFile().getName());
                            entity.setRealParentPath(file.getParent());
                            list.add(entity);
                        } else {
                            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    MediaStore.Images.ImageColumns.DATA + "=?", new String[]{path});
                        }
                    }
                } while (cursor != null && cursor.moveToPrevious());
            } catch (Exception e) {
            }
        }
        Collections.sort(list);

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return list;
    }

    public static List<MediaItem> getDecryptedVideo(Context context) {
        List<MediaItem> list = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, MediaStore.Video.Media.DATE_MODIFIED);
        if (cursor != null && cursor.getColumnCount() != 0 && cursor.getCount() > 0) {
            cursor.moveToLast();
            try {
                do {
                    if (cursor.isNull(cursor.getColumnIndex(MediaStore.Video.Media.DATA))) {
                        break;
                    }
                    MediaItem entity = new MediaItem();
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Video.Media.DATA));
                    long during = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                    if (path != null) {
                        File file = new File(path);
                        if (file != null && file.exists() && file.getParentFile() != null) {
                            entity.setPath(path);
                            entity.setRealPath(path);
                            entity.setSelect(false);
                            entity.setName(file.getName());
                            entity.setRealName(file.getName());
                            entity.setRealParentName(file.getParentFile().getName());
                            entity.setRealParentPath(file.getParent());
                            entity.setDuring(during);
                            list.add(entity);
                        } else {
                            contentResolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    MediaStore.Video.VideoColumns.DATA + "=?", new String[]{path});
                        }
                    }
                } while (cursor != null && cursor.moveToPrevious());
            } catch (IllegalStateException e) {
            }
        }
        Collections.sort(list);

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return list;
    }

    /**
     * @param deletedFiles 删除被加密的文件集合
     * @return
     */
    public static List<String> deleteEncryptedFiles(List<String> deletedFiles) {
        for (String path : deletedFiles) {
            File file = new File(path);
            if (file.isFile()) {
                file.delete();
            }
        }
        return deletedFiles;
    }

    /**
     * @param context
     * @param sourceFiles 要加密的文件
     * @return 成功加密的文件
     */
    public static List<MediaItem> lockPhotos(Context context, List<MediaItem> sourceFiles) {
        ArrayList<MediaItem> fileToDelete = new ArrayList<MediaItem>();
        for (MediaItem sourceFile : sourceFiles) {
            String encrypt = MediaVaultUtil.encryptFilePath(sourceFile.getPath());
            File storage = MediaVaultUtil.createEncryptedFolder(context, true);
            File to = new File(storage, encrypt);
            File from = new File(sourceFile.getPath());
            if (from.exists()) {
                boolean success = from.renameTo(to);
                if (success) {
                    fileToDelete.add(sourceFile);
                    String[] array = new String[]{sourceFile.getPath()};
                    ContentResolver contentResolver = context.getContentResolver();
                    contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            MediaStore.Video.VideoColumns.DATA + "=?", array);

                }
            }
        }
        return fileToDelete;
    }

    public static void lockPhoto(Context context, MediaItem item) {

        String encrypt = MediaVaultUtil.encryptFilePath(item.getPath());
        File storage = MediaVaultUtil.createEncryptedFolder(context, true);
        File to = new File(storage, encrypt);
        File from = new File(item.getPath());
        if (from.exists()) {
            //boolean success = from.renameTo(to);
            try {
                FileUtils.moveFile(from, to);
            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean success = !from.exists();
            if (success) {
                String[] array = new String[]{item.getPath()};
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Video.VideoColumns.DATA + "=?", array);

            }
        }
    }

    public static void lockVideo(Context context, MediaItem item) {

        String encrypt = MediaVaultUtil.encryptFilePath(item.getPath());
        File storage = MediaVaultUtil.createEncryptedFolder(context, false);
        File to = new File(storage, encrypt);
        File from = new File(item.getPath());
        if (from.exists()) {
            //boolean success = from.renameTo(to);
            try {
                FileUtils.moveFile(from, to);
            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean success = !from.exists();
            if (success) {
                String[] array = new String[]{item.getPath()};
                ContentResolver contentResolver = context.getContentResolver();
                try {
                    contentResolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            MediaStore.Video.VideoColumns.DATA + "=?", array);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * @param context
     * @param sourceFiles 需要被解密的文件（FileInfo封装的）
     * @return
     */
    public static List<MediaItem> unlockPhotos(Context context, List<MediaItem> sourceFiles) {
        ArrayList<MediaItem> fileToDecript = new ArrayList<>();
        for (MediaItem sourceFile : sourceFiles) {
            File file = new File(sourceFile.getPath());
            if (file.exists() && file.isFile()) {
                String name = file.getName();
                String realPath = decryptFilePath(name);
                if (TextUtils.isEmpty(realPath)) {
                    continue;
                }
                File realFile = new File(realPath);
                realFile.renameTo(realFile);
                addImageToGallery(context.getContentResolver(), realFile);
                fileToDecript.add(sourceFile);
            }
        }
        return fileToDecript;
    }

    public static void unlockPhoto(Context context, MediaItem photoItem) {
        File file = new File(photoItem.getPath());
        if (file.exists() && file.isFile()) {
            String name = file.getName();
            String realPath = decryptFilePath(name);
            if (TextUtils.isEmpty(realPath)) {
                return;
            }
            File realFile = new File(realPath);
            try {
                FileUtils.moveFile(file, realFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addImageToGallery(context.getContentResolver(), realFile);
        }
    }

    public static void unlockVideo(Context context, MediaItem photoItem) {
        File file = new File(photoItem.getPath());
        long during = photoItem.getDuring();
        if (file.exists() && file.isFile()) {
            String name = file.getName();
            String realPath = decryptFilePath(name);
            if (TextUtils.isEmpty(realPath)) {
                return;
            }
            File realFile = new File(realPath);
            try {
                FileUtils.moveFile(file, realFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addVideoToGallery(context.getContentResolver(), realFile, during);
        }
    }

    public static void trashPhoto(MediaItem photoItem) {
        File file = new File(photoItem.getPath());
        FileUtils.deleteQuietly(file);
    }

    public static Uri addImageToGallery(ContentResolver cr, File filepath) {
        String simpleName = filepath.getName();
        int lastIndex = simpleName.lastIndexOf(".");
        if (lastIndex > -1) {
            String ext = simpleName.substring(lastIndex + 1).toLowerCase();
            if (ext.equals("jpg")) {
                ext = "jpeg";
            }
            String simple = simpleName.substring(0, lastIndex);

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, simple);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, simple);
            values.put(MediaStore.Images.Media.DESCRIPTION, simple);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + ext);
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DATA, filepath.toString());
            try {
                return cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (Exception e) {
                e.printStackTrace();
                //Crashlytics.logException(e);
                return null;
            }

        } else {
            return null;
        }
    }

    public static Uri addVideoToGallery(ContentResolver cr, File filepath, long during) {
        String simpleName = filepath.getName();
        int lastIndex = simpleName.lastIndexOf(".");
        if (lastIndex > -1) {
            String ext = simpleName.substring(lastIndex + 1).toLowerCase();
            if (ext.equals("mp4")) {
                ext = "mp4";
            }
            String simple = simpleName.substring(0, lastIndex);

            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, simple);
            values.put(MediaStore.Video.Media.DISPLAY_NAME, simple);
            values.put(MediaStore.Video.Media.DESCRIPTION, simple);
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/" + ext);
            values.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis());
            values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Video.Media.DATA, filepath.toString());
            values.put(MediaStore.Video.Media.DURATION, during);
            try {
                return cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            } catch (Exception e) {
                e.printStackTrace();
                //Crashlytics.logException(e);
                return null;
            }

        } else {
            return null;
        }
    }

    //将旧版的applock的photo都解锁
    public static void moveToPhoto(Context context) {
        File root = Environment.getExternalStorageDirectory();
        String packageName = context.getPackageName();
        File nomediaFolder = new File(root, packageName + "/" + NO_MEDIA);
        nomediaFolder.mkdirs();
        File nomediaFile = new File(nomediaFolder, NO_MEDIA);
//        System.out.println("nomediaFile.getAbsolutePath() ----------- " + nomediaFile.getAbsolutePath());
        try {
            nomediaFile.createNewFile();
//            System.out.println("MediaVaultUtil.moveToPhoto------1");
        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println("MediaVaultUtil.moveToPhoto------2");
        }
//        System.out.println("MediaVaultUtil.moveToPhoto-------3");
        if (nomediaFolder.exists() && nomediaFolder.isDirectory()) {
//            System.out.println("MediaVaultUtil.moveToPhoto ----------- ");
            File[] files = nomediaFolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return !filename.endsWith(NO_MEDIA);
                }
            });

            File desFile = createEncryptedFolder(context, true);
            if (desFile.exists() && desFile.isDirectory()) {
//                System.out.println("MediaVaultUtil.moveToPhoto-------------4");
                for (File file : files) {
                    try {
                        FileUtils.moveToDirectory(file, desFile, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
