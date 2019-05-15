package app.pic.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PicServerApplication{

    private static final Logger LOG = LogManager.getLogger( PicServerApplication.class );

    public static void main( String... args ){

        SpringApplication.run( PicServerApplication.class, args );
        LOG.debug( "Debugging log in our greeting method" );
        LOG.info( "Info log in our greeting method" );
        LOG.warn( "Warning log in our greeting method" );
        LOG.error( "Error in our greeting method" );
        LOG.fatal( "Damn! Fatal error. Please fix me." ); 
        
    }
}
