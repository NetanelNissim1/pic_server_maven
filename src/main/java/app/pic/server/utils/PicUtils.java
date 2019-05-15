package app.pic.server.utils;

import app.pic.server.common.exceptions.IllegalParameterException;
import static app.pic.server.core.StaticContextInitializer.CLI_PIC_BASE_PATH;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PicUtils{

    private static final Logger LOG = LogManager.getLogger( PicUtils.class );
    public static final String SMALL_PIC_SUFFIX = "_s";
    public static final String DEFAULT_USERPIC_STRING = "pic";
    public static final String DEFAULT_SOCIALPIC_STRING = "soc";
    public static final String PIC_FILENAME_EXTENSION = ".jpg";

    public static final int BIG_PIC_W = 1080;//optimum big pic width
    public static final int BIG_PIC_H = 1920;//optimum big pic hight

    public static final int SMALL_PIC_W = 360;//optimum small pic width
    public static final int SMALL_PIC_H = 480;//optimum small pic height

    public enum PicType{
        ANY_PIC( 0 ),
        USER_PIC( 1 ),
        SOCIAL_FIXED_PIC( 2 );

        private final int picTypeCode;

        PicType( int picTypeCode ){
            this.picTypeCode = picTypeCode;
        }

        public int getCode(){
            return picTypeCode;
        }

        public static PicType getPicTypeByCode( Integer picTypeCode ){
            if( picTypeCode == null ){
                return null;
            }
            for( PicType currPictype : PicType.values() ){
                if( currPictype.getCode() == picTypeCode ){
                    return currPictype;
                }
            }
            return null;
        }
    }

    public enum PicSize{
        SMALL( "small" ),
        BIG( "big" );

        private final String picSizeCode;

        PicSize( String picSizeCode ){
            this.picSizeCode = picSizeCode;
        }

        public String getPicSizeCode(){
            return picSizeCode;
        }

        public static PicSize getPicSizeByCode( String picSizeCode ){
            if( picSizeCode == null ){
                return null;
            }
            for( PicSize currPicSize : PicSize.values() ){
                if( currPicSize.getPicSizeCode().equalsIgnoreCase( picSizeCode ) ){
                    return currPicSize;
                }
            }
            return null;
        }
    }

    public static String getPicPath( String cli, PicSize picSize, PicType pictype ) throws IllegalParameterException{
        String picNameString, picNameSizeSuffix;
        switch( pictype ){

            case USER_PIC:
                picNameString = DEFAULT_USERPIC_STRING;
                break;
            case SOCIAL_FIXED_PIC:
                picNameString = DEFAULT_SOCIALPIC_STRING;
                break;
            default:
                throw new IllegalParameterException( "type " + pictype + " is unsupported" );
        }

        switch( picSize ){
            case SMALL:
                picNameSizeSuffix = SMALL_PIC_SUFFIX;
                break;
            case BIG:
                picNameSizeSuffix = "";
                break;
            default:
                throw new IllegalParameterException( "size " + picSize + " is unsupported" );
        }
        String path = CLI_PIC_BASE_PATH + getDividedPicDirPath( cli ) + picNameString + picNameSizeSuffix + PIC_FILENAME_EXTENSION;
        return path;
    }

    private static String getDividedPicDirPath( String cli ){
        int nDigits = cli.length();
        int nDirs = nDigits / 3;
        int nDigitsInFirstDir = nDigits % 3;

        //e.g. msisdnPic= 972-525-588-123
        String picPath = cli.substring( 0, nDigitsInFirstDir ) + "/";//picPath = 97

        int pointer;
        for( int i = 0; i < nDirs; i++ ){
            pointer = nDigitsInFirstDir + ( i * 3 );
            picPath = picPath + cli.substring( pointer, pointer + 3 ) + "/";
        }
        return picPath;
    }

    public static boolean resizeAndSavePic( BufferedImage bufferedImage, File picFile, int pic_width, int pic_height ) throws IOException{
        boolean res1 = false;
        boolean res2 = false;

        //get the sizes of the picture
        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();

        //resize the pic if needed
        BufferedImage image2Save = bufferedImage;
        if( h > pic_height || w > pic_width ){
            long timeStart = System.currentTimeMillis();
            image2Save = Thumbnails.of( image2Save )
                    .size( pic_width, pic_height )
                    .asBufferedImage();
            //image2Save = Scalr.resize(image2Save, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, pic_width, pic_height); 
            LOG.trace(  "resizing time: " + ( System.currentTimeMillis() - timeStart ) );
        }
        //write the pic to the target file to store it
        res1 = ImageIO.write( image2Save, "jpg", picFile );
        res2 = picFile.isFile();
        return res1 && res2;
    }

    public static File getPicFile( String cli, PicSize picSize, PicType pictype ) throws IllegalParameterException{
        File file = null;

        switch( pictype ){
            case ANY_PIC:
                file = new File( getPicPath( cli, picSize, PicType.USER_PIC ) );
                if( file.isFile() ){
                    break;
                }
                file = new File( getPicPath( cli, picSize, PicType.SOCIAL_FIXED_PIC ) );
                if( file.isFile() == false ){
                    file = null;
                }
                break;
            case USER_PIC:
            case SOCIAL_FIXED_PIC:
                file = new File( getPicPath( cli, picSize, pictype ) );
                if( file.isFile() == false ){
                    file = null;
                }
                break;
            default:
                throw new IllegalParameterException();
        }

        return file;
    }

    public static boolean isUserFixedPicForUser( String cli ){
        try{
            File file = new File( getPicPath( cli, PicSize.BIG, PicType.USER_PIC ) );
            return file.isFile();
        } catch ( IllegalParameterException e ) {
            LOG.error( "Unable to get picPath for cli: " + e.getMessage(), cli );
            return false;
        }
    }
}
