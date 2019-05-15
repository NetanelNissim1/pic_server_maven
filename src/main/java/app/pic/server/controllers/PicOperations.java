package app.pic.server.controllers;

import app.pic.server.common.exceptions.RecordNotFoundException;
import app.pic.server.model.OperationsVO;
import app.pic.server.services.GetPic;
import app.pic.server.services.RemovePicFile;
import app.pic.server.services.SavePicFile;
import app.pic.server.utils.PicUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/img")
public class PicOperations{

    private static final Logger LOGGER = LogManager.getLogger( PicOperations.class );

    @GetMapping(path = "/po/{cli}/{type}/{size}")
    public void getPic(
            @Valid @PathVariable("cli") String cli,
            @Valid @PathVariable("type") String type,
            @Valid @PathVariable("size") String size,
            @Valid @RequestHeader("cliHeader") String cliHeader,
            HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException{

        OperationsVO opertion = new OperationsVO( cli, cliHeader, type, size );

        if( opertion == null ){
            throw new RecordNotFoundException( "Invalid cli or cliHeader or type or size: " + cliHeader );
        }
        GetPic.processRequest( request, response, opertion );
    }

    @PostMapping("/po/{cli}/{type}")
    public ResponseEntity<OperationsVO> save(
            @Valid InputStream in,
            @Valid @RequestHeader("cliHeader") String cliHeader,
            @Valid @PathVariable("cli") String cli,
            @Valid @PathVariable("type") String inType )
            throws IOException{

        OperationsVO opertion = new OperationsVO();
        opertion.setCli( cli );
        opertion.setType( inType );
        opertion.setCliHeader( cliHeader );

        if( opertion == null ){
            LOGGER.error( cliHeader, "Cli or pic or type is null ", "doPost ", "cli: " + cliHeader );
            throw new RecordNotFoundException( "Cli or type is null " + cliHeader );
        }

        PicUtils.PicType type = PicUtils.PicType.getPicTypeByCode( Integer.valueOf( opertion.getType() ) );
        BufferedImage bufferedImage = ImageIO.read( in );
        if( bufferedImage != null ){
            if( SavePicFile.savePicFile( cliHeader, cli, bufferedImage, type ) == false ){
                LOGGER.error( cliHeader, "Couldn't save file to cli: ", cli, " from function savePicFile" );
                throw new RecordNotFoundException( "Couldn't save file to cli: " + opertion.getCli() + " from function savePicFile" );
            }
        } else {
            LOGGER.error( cliHeader, "Couldn't save file to cli: ", cli, " BufferedImage is null!!!" );
            throw new RecordNotFoundException( "Couldn't save file to cli: " + opertion.getCli() + " BufferedImage is null!!!" );
        }
        LOGGER.info( cliHeader, "Pic File saved successfully to cli: ", opertion.getCli(), " And type: " + opertion.getType() );
        opertion.setMessage( cliHeader, "Pic File saved successfully to cli: " + opertion.getCli(), "Type: " + opertion.getType() );
        return new ResponseEntity<>( opertion, HttpStatus.OK );
    }

    @DeleteMapping("/po/{cli}/{type}")
    public ResponseEntity<OperationsVO> delete(
            @Valid @RequestHeader("cliHeader") String cliHeader,
            @Valid @PathVariable("cli") String cli,
            @Valid @PathVariable("type") String inType ){

        OperationsVO opertion = new OperationsVO();
        opertion.setCli( cli );
        opertion.setType( inType );
        opertion.setCliHeader( cliHeader );

        if( opertion == null ){
            LOGGER.error( cliHeader, "Cli or pic or type is null ", "doPost ", "cli: " + cliHeader );
            throw new RecordNotFoundException( "Cli or type is null " + cliHeader );
        }
        PicUtils.PicType type = PicUtils.PicType.getPicTypeByCode( Integer.valueOf( opertion.getType() ) );
        opertion.setnFilesDeleted( RemovePicFile.remove( opertion.getCli(), type ) );
        if( opertion.getnFilesDeleted() == 2 ){
            LOGGER.info( cliHeader, "Successfully deleted photo, from cli: ", cli, " Numbers File deleted: ", String.valueOf( opertion.getnFilesDeleted() ) );
            opertion.setMessage( cliHeader, "Successfully deleted photo, from cli: " + cli, "Numbers File deleted: " + String.valueOf( opertion.getnFilesDeleted() ) );
        } else {
            LOGGER.info( cliHeader, "File does not exist, from cli: ", cli, " Numbers File deleted: ", String.valueOf( opertion.getnFilesDeleted() ) );
            opertion.setMessage( cliHeader, "File does not exist, from cli: " + cli, "Numbers File deleted: " + String.valueOf( opertion.getnFilesDeleted() ) );
        }
        return new ResponseEntity<>( opertion, HttpStatus.OK );
    }
}
