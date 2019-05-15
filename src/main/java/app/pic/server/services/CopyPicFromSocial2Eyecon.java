package app.pic.server.services;

import app.pic.server.core.StaticContextInitializer;
import app.pic.server.utils.PicUtils;
import java.io.File;
import java.nio.file.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CopyPicFromSocial2Eyecon{

    private static final Logger LOG = LogManager.getLogger( CopyPicFromSocial2Eyecon.class );

    public static boolean doCopy( String cli ){
        LOG.trace( cli, "CopyPicFromSocial2Eyecon" );
        if( PicUtils.isUserFixedPicForUser( cli ) ){
            return false;
        } else {
            try{
                File fromFileSmall = PicUtils.getPicFile( cli, PicUtils.PicSize.SMALL, PicUtils.PicType.SOCIAL_FIXED_PIC );
                File fromFileBig = PicUtils.getPicFile( cli, PicUtils.PicSize.BIG, PicUtils.PicType.SOCIAL_FIXED_PIC );

                File toFileSmall = new File( PicUtils.getPicPath( cli, PicUtils.PicSize.SMALL, PicUtils.PicType.USER_PIC ) );
                File toFileBig = new File( PicUtils.getPicPath( cli, PicUtils.PicSize.BIG, PicUtils.PicType.USER_PIC ) );

                Files.copy( fromFileBig.toPath(), toFileBig.toPath() );
                Files.copy( fromFileSmall.toPath(), toFileSmall.toPath() );
                return true;
            } catch ( Exception e ) {
                return false;
            }
        }
    }

}
