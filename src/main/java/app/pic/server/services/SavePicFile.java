package app.pic.server.services;

import app.pic.server.common.exceptions.IllegalParameterException;
import app.pic.server.utils.PicUtils;
import app.pic.server.utils.PicUtils.PicSize;
import app.pic.server.utils.PicUtils.PicType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SavePicFile{

    private static final Logger LOG = LogManager.getLogger( SavePicFile.class );

    public static boolean savePicFile( String cliHeader, String cli, BufferedImage bufferedImage, PicType pictype ) throws IOException{
        boolean isBigPicSaved, isSmallPicSaved;
        File smallPicFile, bigPicFile;
        try{
            String bigPicPath = PicUtils.getPicPath( cli, PicSize.BIG, pictype );
            String smallPicPath = PicUtils.getPicPath( cli, PicSize.SMALL, pictype );
            // preparation of pathes
            bigPicFile = new File( bigPicPath );
            File picDir = bigPicFile.getParentFile();
            // directory structure creation
            if( !picDir.exists() && !picDir.mkdirs() ){
                LOG.warn( cliHeader, "Couldn't create dir: " + picDir, "resizeAndSavePic" );
                return false;
            }
            smallPicFile = new File( smallPicPath );
        } catch ( IllegalParameterException e ) {
            LOG.error( cliHeader, "savePicFile ERR: cli=" + cli + " " + e.getMessage(), "savePicFile" );
            return false;
        }

        // Saving Order is important! Bigger pic first!!!
        isBigPicSaved = PicUtils.resizeAndSavePic( bufferedImage, bigPicFile, PicUtils.BIG_PIC_W, PicUtils.BIG_PIC_H );
        isSmallPicSaved = PicUtils.resizeAndSavePic( bufferedImage, smallPicFile, PicUtils.SMALL_PIC_W, PicUtils.SMALL_PIC_H );

        return isBigPicSaved && isSmallPicSaved;
    }
}
