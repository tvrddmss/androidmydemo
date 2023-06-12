package esa.myweather;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: mydemo
 * @Package: esa.myweather
 * @ClassName: TTFCodeParse
 * @Description: java类作用描述
 * @Author: tvrddmss
 * @CreateDate: 2023/6/2 15:38
 * @UpdateUser: tvrddmss
 * @UpdateDate: 2023/6/2 15:38
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TTFCodeParse {

    public TTFCodeParse() {
        // TODO Auto-generated constructor stub
    }

    public static int COPYRIGHT = 0;

    public static int FAMILY_NAME = 1;

    public static int FONT_SUBFAMILY_NAME = 2;

    public static int UNIQUE_FONT_IDENTIFIER = 3;

    public static int FULL_FONT_NAME = 4;

    public static int VERSION = 5;

    public static int POSTSCRIPT_NAME = 6;

    public static int TRADEMARK = 7;

    public static int MANUFACTURER = 8;

    public static int DESIGNER = 9;

    public static int DESCRIPTION = 10;

    public static int URL_VENDOR = 11;

    public static int URL_DESIGNER = 12;

    public static int LICENSE_DESCRIPTION = 13;

    public static int LICENSE_INFO_URL = 14;

    public String uni = "";

    private Map<Integer, String> fontProperties = new HashMap<Integer, String>();

    public String getFontName() {

        if (fontProperties.containsKey(FULL_FONT_NAME)) {

            return fontProperties.get(FULL_FONT_NAME);

        } else if (fontProperties.containsKey(FAMILY_NAME)) {

            return fontProperties.get(FAMILY_NAME);

        } else {

            return null;

        }

    }

    public String getFontPropertie(int nameID) {

        if (fontProperties.containsKey(nameID)) {

            return fontProperties.get(nameID);

        } else {

            return null;
        }

    }

    public Map<Integer, String> getFontProperties() {

        return fontProperties;

    }

    public String parseInner(RandomAccessFile randomAccessFile) throws IOException {


//        RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "r");

        InputStream inputStream;

        ArrayList<String> arrayList = new ArrayList<String>();

        // Attribute attribute = new Attribute();

        int majorVersion = randomAccessFile.readShort();

        int minorVersion = randomAccessFile.readShort();

        int numOfTables = randomAccessFile.readShort();

        // if (majorVersion != 1 || minorVersion != 0) {
        // return ;
        // }

        // jump to TableDirectory struct

        randomAccessFile.seek(12);

        boolean found = false;

        byte[] buff = new byte[4];

        TableDirectory tableDirectory = new TableDirectory();

        for (int i = 0; i < numOfTables; i++) {

            randomAccessFile.read(buff);

            tableDirectory.name = new String(buff);

            tableDirectory.checkSum = randomAccessFile.readInt();

            tableDirectory.offset = randomAccessFile.readInt();

            tableDirectory.length = randomAccessFile.readInt();

            // System.out.println("*******************" + tableDirectory.name);

            if ("post".equalsIgnoreCase(tableDirectory.name)) {

                found = true;
                // System.out.println("talbe: post found!");
                // break;

            } else if (tableDirectory.name == null || tableDirectory.name.length() == 0) {

                break;

            }

        }

        // not found table of name
        //
        // if (!found) {
        // return;
        // }

        randomAccessFile.seek(tableDirectory.offset);

        if (found) {

            PostTableHeader postTableHeader = new PostTableHeader();
            postTableHeader.format = ttfGetFixed(randomAccessFile);
            postTableHeader.italicAngle = ttfGetFixed(randomAccessFile);

            postTableHeader.underlinePosition = ttfGetSHORT(randomAccessFile);
            postTableHeader.underlineThichness = ttfGetSHORT(randomAccessFile);

            postTableHeader.isFixedPitch = ttfGetLONG(randomAccessFile);
            postTableHeader.minMemType42 = ttfGetLONG(randomAccessFile);
            postTableHeader.maxMemType42 = ttfGetLONG(randomAccessFile);
            postTableHeader.minMemType1 = ttfGetLONG(randomAccessFile);
            postTableHeader.maxMemType1 = ttfGetLONG(randomAccessFile);

            if (postTableHeader.format == 0x00020000) {

                int numGlyphs = ttfGetSHORT(randomAccessFile);

                int[] glyphNameIndex = new int[numGlyphs];

                for (int i = 0; i < numGlyphs; i++) {

                    glyphNameIndex[i] = ttfGetSHORT(randomAccessFile);

                }

                // long pos = randomAccessFile.getFilePointer();
                // randomAccessFile.seek(pos + 1);

                randomAccessFile.skipBytes(1);

                for (int i = 0; i < numGlyphs; i++) {

                    if (glyphNameIndex[i] <= 257) {

                        /* do nothing for standard Mac glyf name */
                    } else if (glyphNameIndex[i] <= 32767) {
                        /*
                         * non-standard glyf name is stored as a Pascal string
                         * in the file i.e. the first byte is the length of the
                         * string but the string is not ended with a null
                         * character
                         */

                        int len = ttfGetCHAR(randomAccessFile);
                        byte[] bf = new byte[len];

                        if (len > 0)
                            randomAccessFile.read(bf);

                        String uniCoding = new String(bf, Charset.forName("utf-8"));

                        arrayList.add(uniCoding);

                    }

                }

            }

        }
        // arrayList.toString().substring(4, 92).replaceAll(" ", "");
        // System.out.println(arrayList.toString().substring(4, 92).replaceAll("
        // ", ""));

        String maoyanencode = arrayList.toString().substring(4, 92).replaceAll(" ", "").
                replaceAll("uni", "")
                .toLowerCase();

        return maoyanencode;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return fontProperties.toString();
    }

    public static int ttfGetCHAR(RandomAccessFile file) {

        int cc = 0;

        try {

            cc = file.readUnsignedByte();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return cc;
    }

    public static int ttfGetSHORT(RandomAccessFile file) {

        int cc = 0;

        try {
            cc = file.readUnsignedByte() << 8;
            cc |= file.readUnsignedByte();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return (int) cc;
    }

    public static long ttfGetLONG(RandomAccessFile file) {

        int cc = 0;

        try {
            cc = file.readUnsignedByte() << 24;
            cc |= file.readUnsignedByte() << 16;
            cc |= file.readUnsignedByte() << 8;
            cc |= file.readUnsignedByte();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return (long) cc;
    }

    public static long ttfGetFixed(RandomAccessFile file) {

        return (long) ttfGetLONG(file);
    }

    public void FixedSplit(long f, long b[]) {

        b[0] = f & 0xff00;
        b[1] = f >> 16;
    }
}
