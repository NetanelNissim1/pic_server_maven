package app.pic.server.services;

import app.pic.server.common.exceptions.IllegalParameterException;
import app.pic.server.common.exceptions.RecordNotFoundException;
import app.pic.server.model.OperationsVO;
import app.pic.server.utils.PicUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Nati
 */
public class GetPic{

    private static final Logger LOG = LogManager.getLogger( GetPic.class );

    public static void processRequest( HttpServletRequest request, HttpServletResponse response, OperationsVO opertion ) throws IOException{

        PicUtils.PicSize size = PicUtils.PicSize.getPicSizeByCode( opertion.getSize() );
        PicUtils.PicType type = PicUtils.PicType.getPicTypeByCode( Integer.valueOf( opertion.getType() ) );
        File picFile;
        try{
            picFile = PicUtils.getPicFile( opertion.getCli(), size, type );
            if( picFile == null ){
                throw new RecordNotFoundException( opertion.getCliHeader() + " Couldn't get file from cli: " + opertion.getCli() + "ile is null " );
            }
        } catch ( IllegalParameterException ex ) {
            throw new RecordNotFoundException( opertion.getCliHeader() + " Couldn't get file method getPicFile  from cli: " + opertion.getCli() );
        }
        response.setContentType( "image/jpg" );
        response.setContentLengthLong( picFile.length() );
        Path path = picFile.toPath();
        request.setAttribute( "org.apache.tomcat.sendfile.support", Boolean.TRUE );
        request.setAttribute( "org.apache.tomcat.sendfile.filename", path.toAbsolutePath().toString() );
        request.setAttribute( "org.apache.tomcat.sendfile.start", 0L );
        request.setAttribute( "org.apache.tomcat.sendfile.end", Files.size( path ) );
        LOG.info( opertion.getCliHeader(), "Get Pic successfully to cli: ", opertion.getCli(), " And type: " + opertion.getType(), " And size " + opertion.getSize() );
    }
}
