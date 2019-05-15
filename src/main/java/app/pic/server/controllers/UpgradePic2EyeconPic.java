package app.pic.server.controllers;

import app.pic.server.common.exceptions.RecordNotFoundException;
import app.pic.server.model.OperationsVO;
import app.pic.server.services.CopyPicFromSocial2Eyecon;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/img")
public class UpgradePic2EyeconPic{

    private static final Logger LOGGER = LogManager.getLogger( UpgradePic2EyeconPic.class );

    @GetMapping(path = "/up2e/{cli}")
    public ResponseEntity<OperationsVO> CopyPicFromSocial2Eyecon(
            @Valid @PathVariable("cli") String cli,
            @Valid @RequestHeader("cliHeader") String cliHeader ){

        OperationsVO opertion = new OperationsVO();
        opertion.setCli( cli );
        opertion.setCliHeader( cliHeader );

        if( opertion == null ){
            LOGGER.error( "Invalid cli : " + cli );
            throw new RecordNotFoundException( "Invalid cli : " + cli );
        }
        if( !CopyPicFromSocial2Eyecon.doCopy( cli ) ){
            LOGGER.error( "Copy Pic from Social 2 Eyecon false UpgradePic2EyeconPic" );
            throw new RecordNotFoundException( "Copy Pic from Social 2 Eyecon false UpgradePic2EyeconPic" );
        }
        LOGGER.info( HttpStatus.OK );
        return new ResponseEntity<>( opertion, HttpStatus.OK );
    }
}
