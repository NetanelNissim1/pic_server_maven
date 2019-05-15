package app.pic.server.services;

import app.pic.server.common.exceptions.IllegalParameterException;
import app.pic.server.utils.PicUtils.PicSize;
import app.pic.server.utils.PicUtils.PicType;
import static app.pic.server.utils.PicUtils.PicType.ANY_PIC;
import static app.pic.server.utils.PicUtils.PicType.SOCIAL_FIXED_PIC;
import static app.pic.server.utils.PicUtils.PicType.USER_PIC;
import static app.pic.server.utils.PicUtils.getPicPath;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemovePicFile{

    private static final Logger LOG = LogManager.getLogger( RemovePicFile.class );

    public static int remove( String cli, PicType pictype ){

        int nFilesDeleted = 0;
        File file;
        try{
            switch( pictype ){
                case ANY_PIC:
                    file = new File( getPicPath( cli, PicSize.BIG, PicType.USER_PIC ) );
                    if( file.exists() && file.isFile() ){
                        file.delete();
                        nFilesDeleted++;
                    }

                    file = new File( getPicPath( cli, PicSize.SMALL, PicType.USER_PIC ) );
                    if( file.exists() && file.isFile() ){
                        file.delete();
                        nFilesDeleted++;
                    }

                    file = new File( getPicPath( cli, PicSize.BIG, PicType.SOCIAL_FIXED_PIC ) );
                    if( file.exists() && file.isFile() ){
                        file.delete();
                        nFilesDeleted++;
                    }

                    file = new File( getPicPath( cli, PicSize.SMALL, PicType.SOCIAL_FIXED_PIC ) );
                    if( file.exists() && file.isFile() ){
                        file.delete();
                        nFilesDeleted++;
                    }
                    break;
                case USER_PIC:
                case SOCIAL_FIXED_PIC:
                    file = new File( getPicPath( cli, PicSize.BIG, pictype ) );
                    if( file.exists() && file.isFile() ){
                        file.delete();
                        nFilesDeleted++;
                    }
                    file = new File( getPicPath( cli, PicSize.SMALL, pictype ) );
                    if( file.exists() && file.isFile() ){
                        file.delete();
                        nFilesDeleted++;
                    }
                    break;
                default:
                    break;
            }
        } catch ( IllegalParameterException e ) {
            LOG.error( cli, "Unable to get picPath for cli: " + e.getMessage(), cli );
        }
        return nFilesDeleted;
    }
}
